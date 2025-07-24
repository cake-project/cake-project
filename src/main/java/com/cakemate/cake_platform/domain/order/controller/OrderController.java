package com.cakemate.cake_platform.domain.order.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.order.dto.OrderCreateRequestDto;
import com.cakemate.cake_platform.domain.order.dto.OrderCreateResponseDto;
import com.cakemate.cake_platform.domain.order.dto.OrderDetailResponseDto;
import com.cakemate.cake_platform.domain.order.dto.OrderPageResponseDto;
import com.cakemate.cake_platform.domain.order.service.OrderService;
import io.jsonwebtoken.Claims;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 주문 생성 API
     * @param requestFormId 주문 대상 의뢰서 ID
     * @param proposalFormId 주문 대상 견적서 ID
     * @param requestDto 소비자에게 주문 정보 입력받는 DTO
     * @return
     */
    @PostMapping("request-forms/{requestFormId}/proposal-forms/{proposalFormId}/accept")
    public ApiResponse<OrderCreateResponseDto> createOrderAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable Long requestFormId,
            @PathVariable Long proposalFormId,
            @RequestBody OrderCreateRequestDto requestDto
    ) {
        // 토큰에서 customerId 가져오기
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long customerId = jwtUtil.subjectMemberId(claims);

        OrderCreateResponseDto responseDto = orderService.createOrderService(customerId, requestFormId, proposalFormId, requestDto);
        ApiResponse<OrderCreateResponseDto> response = ApiResponse.success(HttpStatus.CREATED, "주문이 생성되었습니다.", responseDto);
        return response;
    }

    /**
     * 소비자 -> 주문 목록 조회 API
     * @param customerId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/customers/{customerId}/orders")
    public ApiResponse<OrderPageResponseDto<OrderDetailResponseDto>> getOrderPageCustomerAPI(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        int adjustedPage = Math.max(page - 1, 0);

        Pageable pageable = PageRequest.of(adjustedPage, size);

        OrderPageResponseDto<OrderDetailResponseDto> responseDto = orderService.getCustomerOrderPageService(customerId, pageable);
        ApiResponse<OrderPageResponseDto<OrderDetailResponseDto>> response = ApiResponse.success(HttpStatus.OK, "주문 내역 조회가 완료되었습니다.", responseDto);
        return response;
    }
}
