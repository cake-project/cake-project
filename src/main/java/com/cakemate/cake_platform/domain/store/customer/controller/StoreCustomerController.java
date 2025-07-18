package com.cakemate.cake_platform.domain.store.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.store.customer.dto.StoreSummaryResponseDto;
import com.cakemate.cake_platform.domain.store.customer.service.StoreCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StoreCustomerController {
    private final StoreCustomerService storeCustomerService;

    public StoreCustomerController(StoreCustomerService storeCustomerService) {
        this.storeCustomerService = storeCustomerService;
    }
    @GetMapping("/customer/stores")
    public ResponseEntity<ApiResponse<List<StoreSummaryResponseDto>>> getStoreList(
            @RequestParam(required = false) String address) {
        List<StoreSummaryResponseDto> storeList = storeCustomerService.getStoreList(address);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "가게 목록을 성공적으로 불러왔습니다.", storeList));
    }
}
