package com.cakemate.cake_platform.domain.order.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.order.owner.dto.*;
import com.cakemate.cake_platform.domain.order.owner.service.OrderOwnerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderOwnerController {

    private final OrderOwnerService orderOwnerService;
    private final JwtUtil jwtUtil;

    public OrderOwnerController(OrderOwnerService orderOwnerService, JwtUtil jwtUtil) {
        this.orderOwnerService = orderOwnerService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 점주(가게) -> 주문 상세 조회 API
     */
    @GetMapping("/owners/orders/{orderId}")
    public ApiResponse<OwnerOrderDetailResponseDto> getOwnerStoreOrderDetail(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestParam Long storeId,
            @PathVariable Long orderId
    ) {
        Long ownerId = jwtUtil.extractOwnerId(bearerJwtToken);

        OwnerOrderDetailResponseDto responseDto = orderOwnerService.getOwnerStoreOrderDetail(storeId, ownerId, orderId);
        ApiResponse<OwnerOrderDetailResponseDto> response = ApiResponse.success(HttpStatus.OK, "주문 상세 조회가 완료되었습니다.", responseDto);
        return response;
    }

    /**
     * 점주(가게) -> 주문 목록 조회 API
     */
    @GetMapping("/owners/orders")
    public ApiResponse<OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto>> getOwnerStoreOrderPage(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestParam Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long ownerId = jwtUtil.extractOwnerId(bearerJwtToken);

        int adjustedPage = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(adjustedPage, size);

        OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto> responseDto = orderOwnerService.getOwnerStoreOrderPage(storeId, ownerId, pageable);
        ApiResponse<OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto>> response = ApiResponse.success(HttpStatus.OK, "주문 목록 조회가 완료되었습니다.", responseDto);
        return response;
    }

    /**
     * 점주(가게) -> 주문 상태 수정 API
     *
     * @param bearerJwtToken
     * @param ownerOrderStatusUpdateRequestDto
     */
    @PatchMapping("/owners/orders/{orderId}")
    public ApiResponse<OwnerOrderStatusUpdateResponseDto> updateStoreOrderStatusByOwner(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestParam Long storeId,
            @PathVariable Long orderId,
            @RequestBody OwnerOrderStatusUpdateRequestDto ownerOrderStatusUpdateRequestDto
    ) {
        Long ownerId = jwtUtil.extractOwnerId(bearerJwtToken);

        OwnerOrderStatusUpdateResponseDto responseDto = orderOwnerService.updateStoreOrderStatusByOwner(storeId, orderId, ownerId, ownerOrderStatusUpdateRequestDto);
        ApiResponse<OwnerOrderStatusUpdateResponseDto> response = ApiResponse.success(HttpStatus.OK, "주문 상태 수정이 완료되었습니다.", responseDto);
        return response;
    }
}
