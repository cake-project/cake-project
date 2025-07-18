package com.cakemate.cake_platform.domain.requestForm.customer.repository;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestFormCustomerRepository extends JpaRepository<RequestForm, Long> {
}
