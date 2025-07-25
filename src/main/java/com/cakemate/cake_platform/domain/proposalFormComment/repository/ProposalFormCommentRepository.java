package com.cakemate.cake_platform.domain.proposalFormComment.repository;

import com.cakemate.cake_platform.domain.proposalFormComment.entity.ProposalFormComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProposalFormCommentRepository extends JpaRepository<ProposalFormComment, Long> {

    //삭제되지 않은 특정 댓글을 조회(추후 필요시)
    Optional<ProposalFormComment> findByIdAndIsDeletedFalse(Long id);
}
