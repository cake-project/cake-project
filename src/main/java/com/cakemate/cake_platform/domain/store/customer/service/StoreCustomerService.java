package com.cakemate.cake_platform.domain.store.customer.service;

import com.cakemate.cake_platform.domain.store.customer.repository.StoreCustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreCustomerService {

    private final StoreCustomerRepository storeCustomerRepository;

    public StoreCustomerService(StoreCustomerRepository storeCustomerRepository) {
        this.storeCustomerRepository = storeCustomerRepository;
    }

    public void getStoreList() {

    }
}
