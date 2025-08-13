package com.cakemate.cake_platform.domain.payment.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.payment.customer.dto.CustomerPaymentConfirmRequestDto;
import com.cakemate.cake_platform.domain.payment.customer.dto.CustomerPaymentConfirmResponseDto;
import com.cakemate.cake_platform.domain.payment.customer.service.CustomerPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/payments")
public class CustomerPaymentController {

    private final CustomerPaymentService customerPaymentService;
    private final JwtUtil jwtUtil;

    public CustomerPaymentController(CustomerPaymentService customerPaymentService, JwtUtil jwtUtil) {
        this.customerPaymentService = customerPaymentService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 소비자 -> 결제 승인
     *
     * @param bearerJwtToken
     * @param requestDto
     */
    @PostMapping("/confirm")
    public ApiResponse<CustomerPaymentConfirmResponseDto> confirmCustomerPayment(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestBody CustomerPaymentConfirmRequestDto requestDto
    ) {
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

        CustomerPaymentConfirmResponseDto responseDto = customerPaymentService.confirmCustomerPayment(customerId, requestDto);
        ApiResponse<CustomerPaymentConfirmResponseDto> response = ApiResponse.success(HttpStatus.OK, "결제 승인이 완료되었습니다.", responseDto);
        return response;

    }
}
