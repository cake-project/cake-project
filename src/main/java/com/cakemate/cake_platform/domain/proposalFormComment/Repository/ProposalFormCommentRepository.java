package com.cakemate.cake_platform.domain.proposalFormComment.repository;

import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalFormComment.entity.ProposalFormComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalFormCommentRepository extends JpaRepository<ProposalFormComment, Long> {
    List<ProposalFormComment> findByProposalForm_IdOrderByCreatedAtAsc(Long proposalFormId);
    List<ProposalFormComment> findByProposalForm(ProposalForm proposalForm);
}
