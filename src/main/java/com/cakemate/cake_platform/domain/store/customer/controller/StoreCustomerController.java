package com.cakemate.cake_platform.domain.store.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.store.customer.command.StoreDetailCommand;
import com.cakemate.cake_platform.domain.store.customer.command.StoreSearchCommand;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreCustomerDetailResponseDto;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreSummaryResponseDto;
import com.cakemate.cake_platform.domain.store.customer.service.StoreCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StoreCustomerController {
    private final StoreCustomerService storeCustomerService;
    private final JwtUtil jwtUtil;

    public StoreCustomerController(StoreCustomerService storeCustomerService, JwtUtil jwtUtil) {
        this.storeCustomerService = storeCustomerService;
        this.jwtUtil = jwtUtil;
    }
    @GetMapping("/customer/stores")
    public ResponseEntity<ApiResponse<List<StoreSummaryResponseDto>>> getStoreList(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(required = false) String address) {
        // 토큰 파싱 및 인증 처리
        Long customerId = jwtUtil.extractCustomerId(authorization);
        //서비스 호출
        StoreSearchCommand command = new StoreSearchCommand(customerId, address);
        List<StoreSummaryResponseDto> storeList = storeCustomerService.getStoreList(command);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "가게 목록을 성공적으로 불러왔습니다.", storeList));
    }

    @GetMapping("/customer/store/{storeId}")
    public ResponseEntity<ApiResponse<StoreCustomerDetailResponseDto>> getStoreDetail(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long storeId
    ) {
        //토큰 파싱 및 인증 처리
        Long customerId = jwtUtil.extractCustomerId(authorization);

        //서비스 호출
        StoreDetailCommand command = new StoreDetailCommand(customerId, storeId);
        StoreCustomerDetailResponseDto responseDto = storeCustomerService.getStoreDetail(command);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "가게 정보를 성공적으로 불러왔습니다.", responseDto)
        );
    }
}


