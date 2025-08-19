package com.cakemate.cake_platform.domain.member.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.member.customer.dto.reponse.CustomerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.customer.dto.reponse.UpdateCustomerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.customer.dto.request.UpdateCustomerProfileRequestDto;
import com.cakemate.cake_platform.domain.member.customer.service.CustomerManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

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
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

        return customerManagementService.putUpdateCustomerService(
                customerId, updateCustomerProfileRequestDto
        );
    }

    /**
     * (소비자) 회원 탈퇴 API
     */
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerProfile(
            @RequestHeader("Authorization") String bearerJwtToken
    ) {
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

        ApiResponse<Void> response = customerManagementService.deleteCustomerProfileService(customerId);
        return ResponseEntity.ok(response);
    }
}
