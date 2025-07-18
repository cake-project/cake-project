package com.cakemate.cake_platform.domain.store.owner.repository;

import com.cakemate.cake_platform.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreOwnerRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByOwnerId(Long ownerId);
    List<Store> findByAddressContaining(String address);
}
