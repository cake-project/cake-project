package com.cakemate.cake_platform.domain.store.customer.controller;

import com.cakemate.cake_platform.domain.store.customer.service.StoreCustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController("/api")
public class StoreCustomerController {
    private final StoreCustomerService storeCustomerService;

    public StoreCustomerController(StoreCustomerService storeCustomerService) {
        this.storeCustomerService = storeCustomerService;
    }
    @GetMapping("/customer/stores")
    public void getStoreList() {

    }
}
