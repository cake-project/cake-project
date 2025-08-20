    package com.cakemate.cake_platform.domain.notification.service;

    import com.cakemate.cake_platform.domain.notification.dto.NotificationDto;
    import com.cakemate.cake_platform.domain.notification.entity.Notification;
    import com.cakemate.cake_platform.domain.notification.repository.EmitterRepository;
    import com.cakemate.cake_platform.domain.notification.repository.NotificationRepository;
    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.springframework.data.redis.core.StringRedisTemplate;
    import org.springframework.stereotype.Service;
    import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

    import java.io.IOException;
    import java.time.Duration;
    import java.time.Instant;
    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.util.*;
    import java.util.stream.Collectors;

    @Service
    public class NotificationService {
        private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1시간 타임아웃
        private static final String LAST_CONNECTED_PREFIX = "lastConnectedAt:";
        private static final String NOTIFICATION_PREFIX = "notification:";

        private final EmitterRepository emitterRepository;
        private final NotificationRepository notificationRepository;
        private final StringRedisTemplate redisTemplate;
        private final ObjectMapper objectMapper = new ObjectMapper();


        public NotificationService(EmitterRepository emitterRepository, NotificationRepository notificationRepository, StringRedisTemplate redisTemplate) {
            this.emitterRepository = emitterRepository;
            this.notificationRepository = notificationRepository;
            this.redisTemplate = redisTemplate;
        }

        private NotificationDto createDummyNotification(String memberName) {
            return new NotificationDto(null, "[" + memberName + "]님 SSE 연결 성공", null);
        }

        /**
         * 공통 SSE 연결(구독) 로직
         */
        public SseEmitter subscribe(Long memberId, String memberType) {
            String emitterId = memberType + "_" + memberId + "_" + System.currentTimeMillis();

            //고유 emitterId 만들기(사용자 이름+시간 조합)
            SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

            //emitter 저장
            emitterRepository.save(emitterId, emitter);

            //emitter 생명주기 설정
            emitter.onCompletion(() -> emitterRepository.deleteAllEmitterStartWithId(emitterId));
            emitter.onTimeout(() -> emitterRepository.deleteAllEmitterStartWithId(emitterId));
            emitter.onError((e) -> emitterRepository.deleteAllEmitterStartWithId(emitterId));

            //더미 알림 전송 (연결 확인용)
            sendToClient(emitter, emitterId + "_" + System.currentTimeMillis(), "message",
                    new NotificationDto(null, "[" + memberType + " " + memberId + "] SSE 연결 성공", null));

            //마지막 접속 시점 조회
            LocalDateTime lastConnectedUtc = getLastConnectedAt(memberId, memberType);

            //Redis + DB에서 누락 알림 조회
            List<Notification> missed = getMissedNotifications(memberId, memberType, lastConnectedUtc);

            for (Notification n : missed) {
                sendToClient(emitter, String.valueOf(n.getId()), "message",
                        new NotificationDto(String.valueOf(n.getId()), n.getMessage(), n.getCreatedAt()));
            }

            //마지막 접속 시간 갱신
            redisTemplate.opsForValue().set(LAST_CONNECTED_PREFIX + memberType + ":" + memberId, LocalDateTime.now(ZoneId.of("UTC")).toString());

            return emitter;
        }

        /**
         * 소비자용 SSE 연결(구독)
         */
        public SseEmitter subscribeCustomer(Long memberId) {
            return subscribe(memberId, "customer");
        }

        /**
         * 점주용 SSE 연결(구독)
         */
        public SseEmitter subscribeOwner(Long memberId) {
            return subscribe(memberId, "owner");
        }

        /**
         * 알림 저장 및 실시간 전송(연결 없으면 저장만)
         */
        public void sendNotification(Long receiverId, String message, String memberType) {
            //emitterId 생성
            String emitterId = memberType + "_" + receiverId;

            //알림 저장(DB)
            Notification notification = notificationRepository.save(new Notification(receiverId, memberType, message));

            //알림 저장(Redis) - 3일 TTL
            String key = NOTIFICATION_PREFIX + memberType + ":" + receiverId;
            try {
                String json = objectMapper.writeValueAsString(notification);
                double score = notification.getCreatedAt().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
                redisTemplate.opsForZSet().add(key, json, score);
                redisTemplate.expire(key, Duration.ofDays(3));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Redis serialization error", e);
            }

            //실시간 전송
            emitterRepository.findById(emitterId).ifPresent(emitter -> {
                NotificationDto dto = new NotificationDto(
                        String.valueOf(notification.getId()),
                        notification.getMessage(),
                        notification.getCreatedAt()
                );
                sendToClient(emitter, String.valueOf(notification.getId()), "message", dto);
            });
        }

        /**
         * 마지막 접속 시간 조회
         */
        private LocalDateTime getLastConnectedAt(Long memberId, String memberType) {
            String stored = redisTemplate.opsForValue().get(LAST_CONNECTED_PREFIX + memberType + ":" + memberId);
            if (stored != null) {
                //Redis에 저장된 문자열을 LocalDateTime으로 변환
                return LocalDateTime.parse(stored);
            } else {
                //기본값- 현재 UTC 시간 3일 전
                return LocalDateTime.now(ZoneId.of("UTC")).minusDays(3);
            }
        }

        /**
         * 유실 알림 조회
         */
        private List<Notification> getMissedNotifications(Long memberId, String memberType, LocalDateTime lastConnectedUtc) {
            String key = NOTIFICATION_PREFIX + memberType + ":" + memberId;

            //Redis 조회
            Set<String> cached = redisTemplate.opsForZSet().rangeByScore(
                    key,
                    lastConnectedUtc.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(),
                    Double.MAX_VALUE
            );

            List<Notification> redisNotifications = cached.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, Notification.class);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            //DB 조회
            List<Notification> dbNotifications = notificationRepository.findByReceiverIdAndMemberTypeAndCreatedAtAfter(
                    memberId, memberType, lastConnectedUtc
            );

            //중복 제거 및 정렬
            Map<Long, Notification> merged = new TreeMap<>();
            redisNotifications.forEach(n -> merged.put(n.getId(), n));
            dbNotifications.forEach(n -> merged.put(n.getId(), n));

            return new ArrayList<>(merged.values());
        }

        /**
         *  SSE 이벤트 전송
         */
        private void sendToClient(SseEmitter emitter, String eventId, String eventName, Object data) {
            try {
                emitter.send(SseEmitter.event()
                        .id(eventId)
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
