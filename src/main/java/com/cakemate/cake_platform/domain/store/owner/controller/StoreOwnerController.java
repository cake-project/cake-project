package com.cakemate.cake_platform.domain.store.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.store.owner.dto.StoreCreateRequestDto;
import com.cakemate.cake_platform.domain.store.owner.dto.StoreCreateResponseDto;
import com.cakemate.cake_platform.domain.store.owner.dto.StoreDetailResponseDto;
import com.cakemate.cake_platform.domain.store.owner.service.StoreOwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StoreOwnerController {
    private final StoreOwnerService storeOwnerService;

    public StoreOwnerController(StoreOwnerService storeOwnerService) {
        this.storeOwnerService = storeOwnerService;
    }
    @PostMapping("/owner/stores")
    public ResponseEntity<ApiResponse<StoreCreateResponseDto>> createStore(@RequestBody StoreCreateRequestDto requestDto) {
        StoreCreateResponseDto responseDto = storeOwnerService.createStore(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "가게가 성공적으로 등록되었습니다.", responseDto));
    }

    @GetMapping("/owner/{ownerId}/store")
    public ResponseEntity<ApiResponse<StoreDetailResponseDto>> getStoreDetail(@PathVariable Long ownerId) {
        StoreDetailResponseDto responseDto = storeOwnerService.getStoreDetail(ownerId);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "가게 정보를 성공적으로 불러왔습니다.", responseDto)
        );
    }
}
