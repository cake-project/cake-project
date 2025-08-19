package com.cakemate.cake_platform.domain.store.repository;

import com.cakemate.cake_platform.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByOwnerId(Long ownerId);
    //점주의 자신의 가게 정보 상세조회
    Optional<Store> findByOwnerIdAndIsDeletedFalse(Long ownerId);
    //가게 리스트 조회시 지역 필터가 없는 경우
    List<Store> findByIsDeletedFalseAndIsActiveTrue();
    //가게 상세 정보조회
    Optional<Store> findByIdAndIsDeletedFalseAndIsActiveTrue(Long storeId);
    //가게 리스트 조회시 지역 필터가 있는 경우
    List<Store> findByAddressContainingAndIsDeletedFalseAndIsActiveTrue(String address);
    //가게 수정
    Optional<Store> findByIdAndIsDeletedFalse(Long storeId);
    //가게 중복등록했는지 확인할 때 사용
    boolean existsByOwnerIdAndIsDeletedFalse(Long ownerId);
    //가게 사업자등록번호가 중복되었는지 확인할 때 사용
    boolean existsByBusinessNumber(String businessNumber);
}
