package com.cakemate.cake_platform.domain.proposalForm.scheduler;

import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ProposalFormScheduler {
    private final ProposalFormRepository proposalFormRepository;

    public ProposalFormScheduler(ProposalFormRepository proposalFormRepository) {
        this.proposalFormRepository = proposalFormRepository;
    }

    //confirmed 상태 견적서 자동 취소 기능
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void autoCancelConfirmedProposals() {
        log.info("견적서 자동 취소 스캐쥴러 시작");

        //기준 시간을 현재시간7일전으로 설정
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);

        //ProposalFormStatus.CONFIRMED 상태이고, modifiedAt이 7일 이전인 견적서들을 조회
        List<ProposalForm> expiredProposals = proposalFormRepository
                .findByStatusAndModifiedAtBefore(ProposalFormStatus.CONFIRMED, cutoff);

        //조회된 견적서들을 반복하면서 상태를 CANCELLED로 변경, try-catch로 각 견적서별 예외를 개별 처리
        for (ProposalForm proposalForm : expiredProposals) {
            try {
                proposalForm.canceledStatus(ProposalFormStatus.CANCELLED);
                log.info("자동 취소된 견적서 ID: {}", proposalForm.getId());
            } catch (Exception e) {
                log.warn("자동 취소 실패 - 견적서 ID: {}, 이유: {}", proposalForm.getId(), e.getMessage());
            }
        }
    log.info("자동 취소된 견적서 총 수: {}", expiredProposals.size());
    }
}
