package com.cakemate.cake_platform.domain.store.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.store.owner.dto.*;
import com.cakemate.cake_platform.domain.store.owner.service.StoreOwnerService;
import io.jsonwebtoken.Claims;
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

    @PostMapping("/owner/stores")
    public ResponseEntity<ApiResponse<StoreCreateResponseDto>> createStore(
            @RequestHeader("Authorization") String authorization,
            @RequestBody StoreCreateRequestDto requestDto) {
        String token = jwtUtil.substringToken(authorization);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        StoreCreateResponseDto responseDto = storeOwnerService.createStore(ownerId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "가게가 성공적으로 등록되었습니다.", responseDto));
    }

    @GetMapping("/owner/{ownerId}/store")
    public ResponseEntity<ApiResponse<StoreDetailResponseDto>> getStoreDetail(
            @RequestHeader("Authorization") String authorization) {
        String token = jwtUtil.substringToken(authorization);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        StoreDetailResponseDto responseDto = storeOwnerService.getStoreDetail(ownerId);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "가게 정보를 성공적으로 불러왔습니다.", responseDto)
        );
    }

    @PatchMapping("/owner/store")
    public ResponseEntity<ApiResponse<StoreUpdateResponseDto>> updateStore(
            @RequestHeader("Authorization") String authorization,
            @RequestBody StoreUpdateRequestDto requestDto
    ) {
        String token = jwtUtil.substringToken(authorization);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        StoreUpdateResponseDto responseDto = storeOwnerService.updateStore(ownerId, requestDto);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "가게 정보를 성공적으로 수정했습니다.", responseDto));
    }

    @DeleteMapping("/owner/store")
    public ResponseEntity<ApiResponse<Void>> deleteStore(
            @RequestHeader("Authorization") String authorization
    ) {
        // 1. JWT 토큰에서 ownerId 추출
        String token = jwtUtil.substringToken(authorization);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        // 2. 서비스 호출
        storeOwnerService.deleteStore(ownerId);

        // 3. 성공 응답
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "가게가 성공적으로 삭제되었습니다.", null));
    }
}