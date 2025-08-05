package com.cakemate.cake_platform.domain.payment.repository;

import com.cakemate.cake_platform.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
