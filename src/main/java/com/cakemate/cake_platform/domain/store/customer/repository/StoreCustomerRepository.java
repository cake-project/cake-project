package com.cakemate.cake_platform.domain.store.customer.repository;

import com.cakemate.cake_platform.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCustomerRepository extends JpaRepository<Store, Long> {
}
