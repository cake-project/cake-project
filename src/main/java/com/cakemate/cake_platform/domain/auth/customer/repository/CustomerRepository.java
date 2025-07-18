package com.cakemate.cake_platform.domain.auth.customer.repository;


import com.cakemate.cake_platform.domain.auth.customer.entity.Customer;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Id> {
}
