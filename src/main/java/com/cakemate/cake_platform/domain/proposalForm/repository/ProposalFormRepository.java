package com.cakemate.cake_platform.domain.proposalForm.repository;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProposalFormRepository extends JpaRepository<ProposalForm, Long> {
    // 특정 의뢰서에 연결된 견적서 목록 조회
    List<ProposalForm> findAllByRequestFormIdAndIsDeletedFalse(Long requestFormId);

    // 소비자가 선택한 견적서 외 견적서 목록 조회
    @Query("SELECT p FROM ProposalForm p WHERE p.requestForm.id = :requestFormId AND p.id <> :proposalFormId")
    List<ProposalForm> findOtherProposalsByRequestFormIdExceptSelected(
            @Param("requestFormId") Long requestFormId,
            @Param("proposalFormId") Long proposalFormId
    );

    //하나의 의뢰서에 견적서 여러개 작성 방지
    boolean existsByRequestFormAndOwner(RequestForm requestForm, Owner owner);

    //견적서 최초 등록 시 의뢰서의 상태 ESTIMATING으로 자동 변경
    long countByRequestForm(RequestForm requestForm);

    // 삭제되지 않은 특정 견적서 조회
    Optional<ProposalForm> findByIdAndIsDeletedFalse(Long id);

    //조회 권한이 없는 경우
    List<ProposalForm> findByStore_Owner_Id(Long ownerId);

    // 의뢰서 ID로, 삭제되지 않은 견적서가 하나라도 존재하는지 확인
    boolean existsByRequestFormIdAndIsDeletedFalse(Long requestFormId);

    //견적서 목록 조회 시 10개씩 페이징
//    Page<ProposalForm> findByStore_Owner_IdAndIsDeletedFalse(Long ownerId, Pageable pageable);

    // 해당 의뢰서에 이미 선택된 견적서가 있는지 조회
    boolean existsByRequestFormIdAndStatus(Long requestFormId, ProposalFormStatus proposalFormStatus);

    //CONFIRMED 상태인데 주문이 생성되지 않은 7일 이상 지난 견적서들을 찾는 데 사용됨
    @Query("SELECT pf FROM ProposalForm pf LEFT JOIN Order o ON o.proposalForm = pf " +
            "WHERE pf.status = :status AND pf.modifiedAt < :cutoff AND o IS NULL")
    List<ProposalForm> findByStatusAndModifiedAtBeforeAndNoOrder(
            @Param("status") ProposalFormStatus status,
            @Param("cutoff") LocalDateTime cutoff);


    //삭제 안 된 견적서가 해당 소비자 (customer) 소유인지 확인
    boolean existsByIdAndRequestForm_Customer_IdAndIsDeletedFalse(Long id, Long customerId);

    //삭제 안 된 견적서가 해당 점주(owner) 소유인지 확인
    boolean existsByIdAndOwner_IdAndIsDeletedFalse(Long id, Long ownerId);

    /* 같은 의뢰서에서 이미 ACCEPTED 상태 견적서 존재 여부 */
    boolean existsByRequestForm_IdAndStatusAndIsDeletedFalse(Long requestFormId,
                                                             ProposalFormStatus status);
}
