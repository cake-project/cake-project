package com.cakemate.cake_platform.domain.order.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.order.dto.OrderCreateRequestDto;
import com.cakemate.cake_platform.domain.order.dto.OrderCreateResponseDto;
import com.cakemate.cake_platform.domain.order.dto.OrderPageResponseDto;
import com.cakemate.cake_platform.domain.order.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 생성 API
    @PostMapping("/orders")
    public ApiResponse createOrderAPI(@RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        OrderCreateResponseDto responseDto = orderService.createOrderService(orderCreateRequestDto);
        ApiResponse response = ApiResponse.success(HttpStatus.CREATED, "주문이 정상적으로 완료되었습니다.", responseDto);
        return response;
    }

    // 소비자 -> 주문 목록 조회 API
    @GetMapping("/customers/{customerId}/orders")
    public ApiResponse getOrderPageCustomerAPI(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        int adjustedPage = Math.max(page - 1, 0);

        Pageable pageable = PageRequest.of(adjustedPage, size);

        OrderPageResponseDto responseDto = orderService.getCustomerOrderPageService(customerId, pageable);
        ApiResponse response = ApiResponse.success(HttpStatus.OK, "주문 내역 조회가 완료되었습니다.", responseDto);
        return response;

    }
}
