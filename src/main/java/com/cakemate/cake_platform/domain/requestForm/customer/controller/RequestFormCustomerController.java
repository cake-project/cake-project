package com.cakemate.cake_platform.domain.requestForm.customer.controller;

import com.cakemate.cake_platform.domain.requestForm.customer.service.RequestFormCustomerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class RequestFormCustomerController {
    
    private final RequestFormCustomerService requestFormCustomerService;

    public RequestFormCustomerController(RequestFormCustomerService requestFormCustomerService) {
        this.requestFormCustomerService = requestFormCustomerService;
    }
}
