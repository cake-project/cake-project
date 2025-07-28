package com.cakemate.cake_platform.domain.store.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.store.customer.command.StoreOwnerCommand;
import com.cakemate.cake_platform.domain.store.owner.dto.*;
import com.cakemate.cake_platform.domain.store.owner.service.StoreOwnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StoreOwnerController {
    private final StoreOwnerService storeOwnerService;
    private final JwtUtil jwtUtil;
    public StoreOwnerController(StoreOwnerService storeOwnerService, JwtUtil jwtUtil) {
        this.storeOwnerService = storeOwnerService;
        this.jwtUtil = jwtUtil;
    }
    //가게 등록
    @PostMapping("/owner/stores")
    public ResponseEntity<ApiResponse<StoreCreateResponseDto>> createStore(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid StoreCreateRequestDto requestDto) {
        Long ownerId = jwtUtil.extractOwnerId(authorization);

        StoreOwnerCommand command = new StoreOwnerCommand(ownerId);
        StoreCreateResponseDto responseDto = storeOwnerService.createStore(command, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "가게가 성공적으로 등록되었습니다.", responseDto));
    }
    //가게 상세 조회
    @GetMapping("/owner/store")
    public ResponseEntity<ApiResponse<StoreDetailResponseDto>> getStoreDetail(
            @RequestHeader("Authorization") String authorization) {
        Long ownerId = jwtUtil.extractOwnerId(authorization);

        StoreOwnerCommand command = new StoreOwnerCommand(ownerId);
        StoreDetailResponseDto responseDto = storeOwnerService.getStoreDetail(command);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "가게 정보를 성공적으로 불러왔습니다.", responseDto)
        );
    }
    //가게 수정
    @PatchMapping("/owner/store/{storeId}")
    public ResponseEntity<ApiResponse<StoreUpdateResponseDto>> updateStore(
            @PathVariable Long storeId,
            @RequestHeader("Authorization") String authorization,
            @RequestBody StoreUpdateRequestDto requestDto
    ) {
        Long ownerId = jwtUtil.extractOwnerId(authorization);

        StoreOwnerCommand command = new StoreOwnerCommand(ownerId, storeId);
        StoreUpdateResponseDto responseDto = storeOwnerService.updateStore(command, requestDto);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "가게 정보를 성공적으로 수정했습니다.", responseDto));
    }
    //가게 삭제
    @DeleteMapping("/owner/store/{storeId}")
    public ResponseEntity<ApiResponse<Void>> deleteStore(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long storeId
    ) {
        // 1. JWT 토큰에서 ownerId 추출
        Long ownerId = jwtUtil.extractOwnerId(authorization);

        // 2. 서비스 호출
        StoreOwnerCommand command = new StoreOwnerCommand(ownerId, storeId);
        storeOwnerService.deleteStore(command);

        // 3. 성공 응답
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "가게가 성공적으로 삭제되었습니다.", null));
    }
}