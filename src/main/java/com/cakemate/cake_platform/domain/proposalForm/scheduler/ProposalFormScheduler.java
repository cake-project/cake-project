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
    @Scheduled(cron = "0 0 1 * * *")
    public void autoCancelConfirmedProposals() {
        log.info("견적서 자동 취소 스캐쥴러 시작");

        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        List<ProposalForm> expiredProposals = proposalFormRepository
                .findByStatusAndModifiedAtBefore(ProposalFormStatus.CONFIRMED, cutoff);

        for (ProposalForm proposalForm : expiredProposals) {
            try {
                proposalForm.updateStatus(ProposalFormStatus.CONFIRMED);
                log.info("자동 취소된 견적서 ID: {}", proposalForm.getId());
            } catch (Exception e) {
                log.warn("자동 취소 실패 - 견적서 ID: {}, 이유: {}", proposalForm.getId(), e.getMessage());
            }
        }
    log.info("자동 취소된 견적서 총 수: {}", expiredProposals.size());
    }
}
