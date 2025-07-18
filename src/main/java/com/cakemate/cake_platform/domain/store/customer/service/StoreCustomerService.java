package com.cakemate.cake_platform.domain.store.customer.service;

import com.cakemate.cake_platform.domain.store.customer.dto.StoreSummaryResponseDto;
import com.cakemate.cake_platform.domain.store.customer.repository.StoreCustomerRepository;
import com.cakemate.cake_platform.domain.store.entity.Store;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreCustomerService {

    private final StoreCustomerRepository storeCustomerRepository;

    public StoreCustomerService(StoreCustomerRepository storeCustomerRepository) {
        this.storeCustomerRepository = storeCustomerRepository;
    }

    public List<StoreSummaryResponseDto> getStoreList() {
        List<Store> stores = storeCustomerRepository.findAll();
        return stores.stream()
                .map(StoreSummaryResponseDto::new)
                .collect(Collectors.toList());
    }
}
