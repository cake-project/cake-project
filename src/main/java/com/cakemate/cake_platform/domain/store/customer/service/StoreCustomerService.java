package com.cakemate.cake_platform.domain.store.customer.service;

import com.cakemate.cake_platform.common.exception.StoreNotFoundException;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.store.customer.command.StoreDetailCommand;
import com.cakemate.cake_platform.domain.store.customer.command.StoreSearchCommand;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreCustomerDetailResponseDto;
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
    private final JwtUtil jwtUtil;

    public StoreCustomerService(StoreOwnerRepository storeOwnerRepository, JwtUtil jwtUtil) {
        this.storeOwnerRepository = storeOwnerRepository;
        this.jwtUtil = jwtUtil;
    }
    //가게 리스트 조회(지역 필터)Service
    @Transactional(readOnly = true)
    public List<StoreSummaryResponseDto> getStoreList(StoreSearchCommand storeSearchCommand) {

        List<Store> stores;

        boolean hasAddress = storeSearchCommand.hasAddress();

        if (hasAddress) {
            stores = storeOwnerRepository.findByAddressContaining(storeSearchCommand.getAddress());
        } else {
            stores = storeOwnerRepository.findAll();
        }

        return stores.stream()
                .map(StoreSummaryResponseDto::new)
                .collect(Collectors.toList());
    }
    //가게 상세 조회 Service
    @Transactional(readOnly = true)
    public StoreCustomerDetailResponseDto getStoreDetail(StoreDetailCommand command) {
        Store store = storeOwnerRepository.findById(command.getStoreId())
                .orElseThrow(() -> new StoreNotFoundException("해당 가게를 찾을 수 없습니다."));

        return new StoreCustomerDetailResponseDto(store);
    }
}
