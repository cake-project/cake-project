package com.cakemate.cake_platform.domain.store.customer.service;

import com.cakemate.cake_platform.domain.store.customer.command.StoreSearchCommand;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreSummaryResponseDto;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.owner.repository.StoreOwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreCustomerService {

    private final StoreOwnerRepository storeOwnerRepository;

    public StoreCustomerService(StoreOwnerRepository storeOwnerRepository) {
        this.storeOwnerRepository = storeOwnerRepository;
    }
    //가게 리스트 조회(지역 필터)Service
    @Transactional(readOnly = true)
    public List<StoreSummaryResponseDto> getStoreList(String address) {
        List<Store> stores;

        StoreSearchCommand storeSearchCommand = new StoreSearchCommand(address);
        boolean hasAddress = storeSearchCommand.hasAddress();

        if (hasAddress) {
            stores = storeOwnerRepository.findByAddressContaining(address);
        } else {
            stores = storeOwnerRepository.findAll();
        }

        return stores.stream()
                .map(StoreSummaryResponseDto::new)
                .collect(Collectors.toList());
    }
}
