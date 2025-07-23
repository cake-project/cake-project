package com.cakemate.cake_platform.domain.member.customer.controller;

import com.cakemate.cake_platform.domain.member.customer.service.CustomerManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerManagementController {

    private final CustomerManagementService customerManagementService;

    public CustomerManagementController(CustomerManagementService customerManagementService) {
        this.customerManagementService = customerManagementService;
    }
}
