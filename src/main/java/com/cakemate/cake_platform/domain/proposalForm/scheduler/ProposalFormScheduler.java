package com.cakemate.cake_platform.domain.proposalForm.scheduler;

import com.cakemate.cake_platform.domain.notification.service.NotificationService;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ProposalFormScheduler {
    private final ProposalFormRepository proposalFormRepository;
    private final NotificationService notificationService;

    public ProposalFormScheduler(ProposalFormRepository proposalFormRepository, NotificationService notificationService) {
        this.proposalFormRepository = proposalFormRepository;
        this.notificationService = notificationService;
    }

    //confirmed 상태 견적서 자동 취소 기능
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")//매일 새벽 1시에 실행
    public void autoCancelConfirmedProposals() {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("견적서 자동 취소 스케줄러 시작 - 시작시간: {}", startTime);
        //기준 시간을 현재시간7일전으로 설정
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);

        //ProposalFormStatus.CONFIRMED 상태이고, modifiedAt이 7일 이전인 견적서들을 조회
        List<ProposalForm> expiredProposals = proposalFormRepository
                .findByStatusAndModifiedAtBeforeAndNoOrder(ProposalFormStatus.CONFIRMED, cutoff);
        int cancelCount = 0;
        //조회된 견적서들을 반복하면서 상태를 CANCELLED로 변경, try-catch로 각 견적서별 예외를 개별 처리
        for (ProposalForm proposalForm : expiredProposals) {
            try {
                proposalForm.canceledStatus();
                cancelCount++;  // 여기서 상태 변경 성공 시 카운트 증가
                log.info("자동 취소된 견적서 ID: {}", proposalForm.getId());

                //견적서 취소 알림 보내기(점주에게)
                notificationService.sendNotification(
                        proposalForm.getStore().getOwner().getId(),
                        "견적서가 자동으로 취소되었습니다.",
                        "owner"
                );
            } catch (Exception e) {
                log.warn("자동 취소 실패 - 견적서 ID: {}, 이유: {}", proposalForm.getId(), e.getMessage());
            }

        }
        LocalDateTime endTime = LocalDateTime.now();
        log.info("견적서 자동 취소 스케줄러 종료 - 종료시간: {}", endTime);

        // 진행시간 계산 (밀리초 단위)
        Duration duration = Duration.between(startTime, endTime);
        log.info("자동 취소 진행시간: {}초 ({}밀리초)", duration.toSeconds(), duration.toMillis());

        log.info("실제 상태가 변경된 견적서 수: {}", cancelCount);
    }
}