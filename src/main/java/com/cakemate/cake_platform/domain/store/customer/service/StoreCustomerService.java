package com.cakemate.cake_platform.domain.store.customer.service;

import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.store.customer.command.StoreSearchCommand;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreCustomerDetailResponseDto;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreSummaryResponseDto;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.owner.repository.StoreOwnerRepository;
import io.jsonwebtoken.Claims;
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
    public List<StoreSummaryResponseDto> getStoreList(String address) {
        List<Store> stores;

        StoreSearchCommand storeSearchCommand = new StoreSearchCommand(address);
        boolean hasAddress = storeSearchCommand.hasAddress();

        if (hasAddress) {
            stores = storeOwnerRepository.findByAddressContaining(address);
        } else {
            stores = storeOwnerRepository.findAll();
        }
        // 조회 결과 없을 경우 예외
        if (stores.isEmpty()) {
            throw new IllegalArgumentException("조건에 맞는 가게가 존재하지 않습니다.");
        }
        return stores.stream()
                .map(StoreSummaryResponseDto::new)
                .collect(Collectors.toList());
    }
    //가게 상세 조회 Service
    public StoreCustomerDetailResponseDto getStoreDetail(String authorizationHeader, Long storeId) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // 토큰 파싱 및 검증
        String token = jwtUtil.substringToken(authorizationHeader);
        Claims claims = jwtUtil.verifyToken(token);

        // 토큰에서 회원 ID 추출 (필요하면 여기서 추가 검증 가능)
        Long memberId = jwtUtil.subjectMemberId(claims);

        // 실제 가게 조회
        Store store = storeOwnerRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게가 존재하지 않습니다."));

        return new StoreCustomerDetailResponseDto(store);
    }
}
