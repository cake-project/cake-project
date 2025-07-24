package com.cakemate.cake_platform.domain.proposalForm.repository;

import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProposalFormRepository extends JpaRepository<ProposalForm, Long> {
    // 특정 의뢰서에 연결된 견적서 목록 조회
    List<ProposalForm> findAllByRequestFormIdAndIsDeletedFalse(Long requestFormId);

    // 소비자가 선택한 견적서 외 견적서 목록 조회
    @Query("SELECT p FROM ProposalForm p WHERE p.requestForm.id = :requestFormId AND p.id <> :proposalFormId")
    List<ProposalForm> findOtherProposalsByRequestFormIdExceptSelected(
            @Param("requestFormId") Long requestFormId,
            @Param("proposalFormId") Long proposalFormId
    );

}
