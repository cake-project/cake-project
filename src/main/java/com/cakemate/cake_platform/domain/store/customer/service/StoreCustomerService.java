package com.cakemate.cake_platform.domain.store.customer.service;

import com.cakemate.cake_platform.domain.store.customer.command.SearchCommand;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreSummaryResponseDto;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.owner.repository.StoreOwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreCustomerService {

    private final StoreOwnerRepository storeOwnerRepository;

    public StoreCustomerService(StoreOwnerRepository storeOwnerRepository) {
        this.storeOwnerRepository = storeOwnerRepository;
    }

    public List<StoreSummaryResponseDto> getStoreList(String address) {
        List<Store> stores;

        SearchCommand searchCommand = new SearchCommand(address);
        boolean hasAddress = searchCommand.hasAddress();

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
