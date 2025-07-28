package com.cakemate.cake_platform.domain.order.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.order.owner.dto.OwnerOrderDetailResponseDto;
import com.cakemate.cake_platform.domain.order.owner.dto.OwnerOrderPageResponseDto;
import com.cakemate.cake_platform.domain.order.owner.dto.OwnerOrderSummaryResponseDto;
import com.cakemate.cake_platform.domain.order.owner.service.OrderOwnerService;
import io.jsonwebtoken.Claims;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderOwnerController {

    private final OrderOwnerService ownerService;
    private final JwtUtil jwtUtil;
    private final OrderOwnerService orderOwnerService;

    public OrderOwnerController(OrderOwnerService ownerService, JwtUtil jwtUtil, OrderOwnerService orderOwnerService) {
        this.ownerService = ownerService;
        this.jwtUtil = jwtUtil;
        this.orderOwnerService = orderOwnerService;
    }

    /**
     * 점주 -> 주문 상세 조회 API
     */
    @GetMapping("owners/stores/{storeId}/orders/{orderId}")
    public ApiResponse<OwnerOrderDetailResponseDto> getOwnerStoreOrderDetailAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable Long storeId,
            @PathVariable Long orderId
    ) {
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        OwnerOrderDetailResponseDto responseDto = orderOwnerService.getOwnerOrderDetailService(storeId, ownerId, orderId);
        ApiResponse<OwnerOrderDetailResponseDto> response = ApiResponse.success(HttpStatus.OK, "주문 상세 조회가 완료되었습니다.", responseDto);
        return response;
    }

    /**
     * 점주 -> 주문 목록 조회 API
     */
    @GetMapping("owners/stores/{storeId}/orders")
    public ApiResponse<OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto>> getOwnerStoreOrderPageAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        int adjustedPage = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(adjustedPage, size);

        OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto> responseDto = orderOwnerService.getOwnerStoreOrderPageService(storeId, ownerId, pageable);
        ApiResponse<OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto>> response = ApiResponse.success(HttpStatus.OK, "주문 목록 조회가 완료되었습니다.", responseDto);
        return response;
    }
}
