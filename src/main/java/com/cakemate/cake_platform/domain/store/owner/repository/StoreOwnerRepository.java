package com.cakemate.cake_platform.domain.store.owner.repository;

import com.cakemate.cake_platform.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOwnerRepository extends JpaRepository<Store, Long> {

}
