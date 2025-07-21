package com.cakemate.cake_platform.domain.auth.signup.customer.repository;


import com.cakemate.cake_platform.domain.auth.signup.customer.entity.Customer;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByEmail(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String email);

}
