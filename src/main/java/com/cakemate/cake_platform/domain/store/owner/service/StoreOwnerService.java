package com.cakemate.cake_platform.domain.store.owner.service;

import com.cakemate.cake_platform.domain.store.owner.repository.StoreOwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreOwnerService {
    private final StoreOwnerRepository storeOwnerRepository;

    public StoreOwnerService(StoreOwnerRepository storeOwnerRepository) {
        this.storeOwnerRepository = storeOwnerRepository;
    }

    public void createStore() {

    }
}
