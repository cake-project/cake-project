package com.cakemate.cake_platform.domain.order.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.order.customer.dto.*;
import com.cakemate.cake_platform.domain.order.customer.service.OrderCustomerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderCustomerController {

    private final OrderCustomerService orderService;
    private final JwtUtil jwtUtil;

    public OrderCustomerController(OrderCustomerService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 주문 생성 API
     *
     * @param requestDto 주문자 이름, 의뢰서/견적서 정보 받아옴
     * @return
     */
    @PostMapping("/customers/orders")
    public ApiResponse<CustomerOrderCreateResponseDto> createOrderAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestBody CustomerOrderCreateRequestDto requestDto
    ) {
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);
        Long proposalFormId = requestDto.getProposalFormId();

        CustomerOrderCreateResponseDto responseDto = orderService.createOrderService(customerId, proposalFormId, requestDto);
        ApiResponse<CustomerOrderCreateResponseDto> response = ApiResponse.success(HttpStatus.CREATED, "주문이 생성되었습니다.", responseDto);
        return response;
    }

    /**
     * 소비자 -> 주문 상세 조회 API
     */
    @GetMapping("/customers/orders/{orderId}")
    public ApiResponse<CustomerOrderDetailResponseDto> getCustomerOrderDetailAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable Long orderId
    ) {
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

        CustomerOrderDetailResponseDto responseDto = orderService.getCustomerOrderDetailService(customerId, orderId);
        ApiResponse<CustomerOrderDetailResponseDto> response = ApiResponse.success(HttpStatus.OK, "주문 상세 조회가 완료되었습니다.", responseDto);
        return response;
    }

    /**
     * 소비자 -> 주문 목록 조회 API
     *
     * @param bearerJwtToken
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/customers/orders")
    public ApiResponse<CustomerOrderPageResponseDto<CustomerOrderSummaryResponseDto>> getCustomerOrderPageAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

        int adjustedPage = Math.max(page - 1, 0);

        Pageable pageable = PageRequest.of(adjustedPage, size);

        CustomerOrderPageResponseDto<CustomerOrderSummaryResponseDto> responseDto = orderService.getCustomerOrderPageService(customerId, pageable);
        ApiResponse<CustomerOrderPageResponseDto<CustomerOrderSummaryResponseDto>> response = ApiResponse.success(HttpStatus.OK, "주문 내역 조회가 완료되었습니다.", responseDto);
        return response;
    }
}

