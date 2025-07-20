package com.cakemate.cake_platform.domain.auth.customer.repository;


import com.cakemate.cake_platform.domain.auth.customer.entity.Customer;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
