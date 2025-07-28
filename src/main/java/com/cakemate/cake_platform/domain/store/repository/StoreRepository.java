package com.cakemate.cake_platform.domain.store.repository;

import com.cakemate.cake_platform.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByOwnerId(Long ownerId);

    Optional<Store> findByOwnerIdAndIsDeletedFalse(Long ownerId);
    // isDeleted가 false인 것만 조회
    List<Store> findByIsDeletedFalse();
    //가게 상세 정보조회
    Optional<Store> findByIdAndIsDeletedFalse(Long ownerId);

    List<Store> findByAddressContainingAndIsDeletedFalse(String address);

    //가게 중복등록했는지 확인할 때 사용
    boolean existsByOwnerId(Long ownerId);
    //가게 사업자등록번호가 중복되었는지 확인할 때 사용
    boolean existsByBusinessNumber(String businessNumber);
}
