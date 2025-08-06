package com.cakemate.cake_platform.domain.payment.customer.controller;

import com.cakemate.cake_platform.domain.payment.customer.service.CustomerPaymentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers/payments")
public class CustomerPaymentController {

    private final CustomerPaymentService customerPaymentService;

    public CustomerPaymentController(CustomerPaymentService customerPaymentService) {
        this.customerPaymentService = customerPaymentService;
    }
}
