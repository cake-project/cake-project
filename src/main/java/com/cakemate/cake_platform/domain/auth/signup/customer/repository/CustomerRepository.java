package com.cakemate.cake_platform.domain.auth.signup.customer.repository;


import com.cakemate.cake_platform.domain.auth.entity.Customer;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String email);
    boolean existsByEmail(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String email);


}
