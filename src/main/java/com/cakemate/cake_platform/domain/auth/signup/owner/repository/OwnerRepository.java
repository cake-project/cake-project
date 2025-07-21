package com.cakemate.cake_platform.domain.auth.signup.owner.repository;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByEmail(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String email);
    Optional<Owner> findByEmail(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String email);
}
