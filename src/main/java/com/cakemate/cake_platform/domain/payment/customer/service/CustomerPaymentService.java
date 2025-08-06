package com.cakemate.cake_platform.domain.payment.customer.service;

import com.cakemate.cake_platform.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerPaymentService {

    private final PaymentRepository paymentRepository;

    public CustomerPaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
