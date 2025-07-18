package com.cakemate.cake_platform.domain.requestForm.owner.repository;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestFormOwnerRepository extends JpaRepository<RequestForm, Long> {
}
