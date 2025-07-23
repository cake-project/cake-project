package com.cakemate.cake_platform.domain.member.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.member.owner.dto.OwnerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.owner.service.OwnerManagementService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owners")
public class OwnerManagementController {

    private final OwnerManagementService ownerManagementService;
    private final JwtUtil jwtUtil;

    public OwnerManagementController(OwnerManagementService ownerManagementService, JwtUtil jwtUtil) {
        this.ownerManagementService = ownerManagementService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/me")
    public ApiResponse getOwnerProfileAPI(@RequestHeader("Authorization") String bearerJwtToken) {
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        OwnerProfileResponseDto responseDto = ownerManagementService.getOwnerProfileService(ownerId);
        ApiResponse response = ApiResponse.success(HttpStatus.OK, "회원 정보 조회가 완료되었습니다.", responseDto);
        return response;
    }
}
