package com.cakemate.cake_platform.domain.proposalForm.repository;

import com.cakemate.cake_platform.domain.proposalForm.controller.ProposalForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalFormRepository extends JpaRepository<ProposalForm, Long> {
}
