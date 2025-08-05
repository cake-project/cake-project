package com.cakemate.cake_platform.domain.payment.service;

import com.cakemate.cake_platform.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
