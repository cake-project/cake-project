package com.cakemate.cake_platform.domain.store.customer.service;

import com.cakemate.cake_platform.common.exception.StoreNotFoundException;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.store.customer.command.StoreDetailCommand;
import com.cakemate.cake_platform.domain.store.customer.command.StoreSearchCommand;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreCustomerDetailResponseDto;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreSummaryResponseDto;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreCustomerService {

    private final StoreRepository storeRepository;
    private final JwtUtil jwtUtil;

    public StoreCustomerService(StoreRepository storeRepository, JwtUtil jwtUtil) {
        this.storeRepository = storeRepository;
        this.jwtUtil = jwtUtil;
    }
    //가게 리스트 조회(지역 필터)Service
    @Transactional(readOnly = true)
    public List<StoreSummaryResponseDto> getStoreList(StoreSearchCommand storeSearchCommand) {

        List<Store> stores;

        boolean hasAddress = storeSearchCommand.hasAddress();

        if (hasAddress) {
            stores = storeRepository.findByAddressContainingAndIsDeletedFalseAndIsActiveTrue(storeSearchCommand.getAddress());
        } else {
            stores = storeRepository.findByIsDeletedFalseAndIsActiveTrue();
        }

        return stores.stream()
                .map(StoreSummaryResponseDto::new)
                .collect(Collectors.toList());
    }
    //가게 상세 조회 Service
    @Transactional(readOnly = true)
    public StoreCustomerDetailResponseDto getStoreDetail(StoreDetailCommand command) {
        Store store = storeRepository.findByIdAndIsDeletedFalseAndIsActiveTrue(command.getStoreId())
                .orElseThrow(() -> new StoreNotFoundException("해당 가게를 찾을 수 없습니다."));

        return new StoreCustomerDetailResponseDto(store);
    }
}
