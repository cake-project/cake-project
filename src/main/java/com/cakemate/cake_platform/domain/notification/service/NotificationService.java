package com.cakemate.cake_platform.domain.notification.service;

import com.cakemate.cake_platform.domain.notification.dto.NotificationDto;
import com.cakemate.cake_platform.domain.notification.entity.Notification;
import com.cakemate.cake_platform.domain.notification.repository.EmitterRepository;
import com.cakemate.cake_platform.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1시간 타임아웃
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;


    public NotificationService(EmitterRepository emitterRepository, NotificationRepository notificationRepository) {
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
    }

    private NotificationDto createDummyNotification(String memberName) {
        return new NotificationDto(null, "[" + memberName + "]님 SSE 연결 성공", null);
    }

    /**
     * 소비자용 SSE 연결(구독)
     */
    @Transactional
    public SseEmitter subscribeCustomer(Long memberId, String lastEventId) {
        String emitterId = "customer_" + memberId;

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
                new NotificationDto(null, "[소비자 " + memberId + "] SSE 연결 성공", null));

        //유실된 알림 확인 및 재전송
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }
        //emitter 반환 및 연결 유지
        return emitter;
    }

    /**
     * 점주용 SSE 연결(구독)
     */
    @Transactional
    public SseEmitter subscribeOwner(Long memberId, String lastEventId) {
        String emitterId = "owner_" + memberId;
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(emitterId, emitter);
        emitter.onCompletion(() -> emitterRepository.deleteAllEmitterStartWithId(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteAllEmitterStartWithId(emitterId));
        emitter.onError((e) -> emitterRepository.deleteAllEmitterStartWithId(emitterId));

        sendToClient(emitter, emitterId + "_" + System.currentTimeMillis(), "message",
                new NotificationDto(null, "[점주 " + memberId + "] SSE 연결 성공", null));

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    /**
     * 알림 저장 및 실시간 전송(연결 없으면 저장만)
     */
    public void sendNotification(Long receiverId, String message, String memberType) {
        //emitterId 생성
        String emitterId = memberType + "_" + receiverId;

        //알림 저장
        Notification notification = notificationRepository.save(new Notification(receiverId, message));

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
     * 유실 데이터 존재 여부 체크(lastEventId 기반)
     */
    private boolean hasLostData(String lastEventId) {
        if (lastEventId == null || lastEventId.isEmpty()) {
            return false;
        }
        //lastEventId보다 큰 Id 있으면 유실된 알림 조회
        try {
            Long lastId = Long.parseLong(lastEventId);
            return notificationRepository.existsByIdGreaterThan(lastId);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 유실된 알림 재전송
     */
    private void sendLostData(String lastEventId, Long receiverId, String emitterId, SseEmitter emitter) {
        //lastEventId를 Long 타입으로 변환
        Long lastId = Long.parseLong(lastEventId);
        //1일 전 시점 계산
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        //DB에서 해당 사용자(receiverId)의 lastId 이후에 생성된 1일 이내 알림 리스트를 조회(오름차순)
        List<Notification> missedNotifications = notificationRepository
                .findByReceiverIdAndIdGreaterThanAndCreatedAtAfterOrderByIdAsc(
                        receiverId, lastId, oneDayAgo);

        //조회된 알림을 하나씩 전송
        for (Notification notification : missedNotifications) {
            //DTO 만들기
            NotificationDto dto = new NotificationDto(
                    String.valueOf(notification.getId()),
                    notification.getMessage(),
                    notification.getCreatedAt()
            );
            sendToClient(emitter, String.valueOf(notification.getId()), "message", dto);
        }
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
