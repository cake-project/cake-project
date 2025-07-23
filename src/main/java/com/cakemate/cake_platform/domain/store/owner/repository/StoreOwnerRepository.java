package com.cakemate.cake_platform.domain.store.owner.repository;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreOwnerRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByOwnerId(Long ownerId);

    List<Store> findByAddressContaining(String address);

    Optional<Store> findByOwner(Owner owner);
    //가게 중복등록했는지 확인할 때 사용
    boolean existsByOwnerId(Long ownerId);
}
