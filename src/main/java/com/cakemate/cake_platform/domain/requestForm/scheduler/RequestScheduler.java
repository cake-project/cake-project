package com.cakemate.cake_platform.domain.requestForm.scheduler;

import com.cakemate.cake_platform.domain.notification.service.NotificationService;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.cakemate.cake_platform.domain.requestForm.repository.RequestFormRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class RequestScheduler {

    private final RequestFormRepository requestFormRepository;
    private final NotificationService notificationService;

    public RequestScheduler(RequestFormRepository requestFormRepository, NotificationService notificationService) {
        this.requestFormRepository = requestFormRepository;
        this.notificationService = notificationService;
    }

    /**
     * 견적이 달리지 않은 REQUESTED 상태의 의뢰서 중,
     * desiredPickUpDate가 지난 의뢰서를 매일 새벽 1시에 소프트 딜리트 처리(현재는 test용으로 1분간격으로 되어있음)
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")//테스트용으로 60초로 바꿈
    public void autoSoftDeleteExpiredRequests() {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("의뢰서 자동 소프트 딜리트 스케쥴러 시작 - 시작시간: {}", startTime);

        //기준 시간: 현재 시각
        LocalDateTime now = LocalDateTime.now();
        log.info("현재 기준 시간 now: {}", now);
        //삭제 대상: status = REQUESTED, desiredPickupDate < now, isDeleted = false
        List<RequestForm> expiredRequestForms = requestFormRepository
                .findByStatusAndDesiredPickupDateBeforeAndIsDeletedFalse(RequestFormStatus.REQUESTED, now);

        int deletedCount = 0;

        for (RequestForm requestForm : expiredRequestForms) {
            try {
                log.info("now: {}, desiredPickupDate: {}", LocalDateTime.now(), requestForm.getDesiredPickupDate());
                requestForm.validateAndSoftDeleteForScheduler();
                deletedCount++;
                log.info("자동 소프트 딜리트된 의뢰서 ID: {}", requestForm.getId());

                //의뢰서 취소 알림 보내기(소비자에게)
                notificationService.sendNotification(
                        requestForm.getCustomer().getId(),
                        "의뢰서가 자동으로 취소되었습니다.",
                        "customer"
                );
            }
            catch (Exception e) {
                log.warn("소프트 딜리트 실패 - 의뢰서 ID: {}, 이유: {}", requestForm.getId(), e.getMessage());
            }
        }

        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);

        log.info("의뢰서 자동 소프트 딜리트 스케쥴러 종료 - 종료시간: {}", endTime);
        log.info("총 진행시간: {}초 ({}밀리초)", duration.toSeconds(), duration.toMillis());
        log.info("실제 소프트 딜리트된 의뢰서 수: {}", deletedCount);


    }
}
