package com.cakemate.cake_platform.domain.requestForm.repository;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestFormRepository extends JpaRepository<RequestForm, Long> {

    //특정 의뢰서를 삭제 여부(false) 조건과 함께 조회
    Optional<RequestForm> findByIdAndIsDeletedFalse(Long id);

    //삭제되지 않은 모든 의뢰서 조회
    List<RequestForm> findAllByIsDeletedFalse();

    Page<RequestForm> findByRegionContainingAndIsDeletedFalse(String keyWord, Pageable pageable);


    // 내 의뢰서 전체 조회 (삭제되지 않은 것만), 최신 생성일 순 정렬.
    List<RequestForm> findAllByCustomerIdAndIsDeletedFalseOrderByCreatedAtDesc(Long customerId);

}
