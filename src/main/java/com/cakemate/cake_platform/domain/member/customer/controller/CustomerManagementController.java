package com.cakemate.cake_platform.domain.member.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.member.customer.dto.CustomerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.customer.dto.reponse.UpdateCustomerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.customer.dto.request.UpdateCustomerProfileRequestDto;
import com.cakemate.cake_platform.domain.member.customer.service.CustomerManagementService;
import com.cakemate.cake_platform.domain.member.owner.dto.request.UpdateOwnerProfileRequestDto;
import com.cakemate.cake_platform.domain.member.owner.dto.response.UpdateOwnerProfileResponseDto;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerManagementController {

    private final CustomerManagementService customerManagementService;
    private final JwtUtil jwtUtil;

    public CustomerManagementController(CustomerManagementService customerManagementService, JwtUtil jwtUtil) {
        this.customerManagementService = customerManagementService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 소비자 -> 내 정보 조회 API
     * @param bearerJwtToken
     * @return
     */
    @GetMapping("/me")
    public ApiResponse<CustomerProfileResponseDto> getCustomerProfileAPI(@RequestHeader("Authorization") String bearerJwtToken) {

        // 헤더에서 토큰 추출 후 customerId 꺼내기
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long customerId = jwtUtil.subjectMemberId(claims);

        CustomerProfileResponseDto responseDto = customerManagementService.getCustomerProfileService(customerId);
        ApiResponse<CustomerProfileResponseDto> response = ApiResponse.success(HttpStatus.OK, "회원 정보 조회가 완료되었습니다.", responseDto);
        return response;
    }
    /**
     * (소비자) 내 정보 수정 API
     */
    @PutMapping("/me")
    public ApiResponse<UpdateCustomerProfileResponseDto> putCustomerProfileAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestBody UpdateCustomerProfileRequestDto updateCustomerProfileRequestDto
    ) {
        // JWT 토큰에서 customerId 추출
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long customerId = jwtUtil.subjectMemberId(claims);

        return customerManagementService.putUpdateCustomerService(
                customerId, updateCustomerProfileRequestDto
        );
    }

}
