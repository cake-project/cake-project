package com.cakemate.cake_platform.domain.auth.signup.owner.repository;

import com.cakemate.cake_platform.domain.auth.signup.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
