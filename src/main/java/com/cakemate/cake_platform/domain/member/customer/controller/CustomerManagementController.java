package com.cakemate.cake_platform.domain.member.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.member.customer.dto.CustomerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.customer.service.CustomerManagementService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerManagementController {

    private final CustomerManagementService customerManagementService;
    private final JwtUtil jwtUtil;

    public CustomerManagementController(CustomerManagementService customerManagementService, JwtUtil jwtUtil) {
        this.customerManagementService = customerManagementService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/me")
    public ApiResponse getCustomerProfileAPI(@RequestHeader("Authorization") String bearerJwtToken) {

        // 헤더에서 토큰 추출 후 customerId 꺼내기
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long customerId = jwtUtil.subjectMemberId(claims);

        CustomerProfileResponseDto responseDto = customerManagementService.getCustomerProfileService(customerId);
        ApiResponse response = ApiResponse.success(HttpStatus.OK, "회원 정보 조회가 완료되었습니다.", responseDto);
        return response;
    }
}
