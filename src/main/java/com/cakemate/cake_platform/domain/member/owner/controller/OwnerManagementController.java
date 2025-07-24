package com.cakemate.cake_platform.domain.member.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.member.owner.dto.request.UpdateOwnerProfileRequestDto;
import com.cakemate.cake_platform.domain.member.owner.dto.response.OwnerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.owner.service.OwnerManagementService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
public class  OwnerManagementController {

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

    /**
     * (점주) 내 정보 수정 API
     */
    @PutMapping("/me")
    public void putOwnerProfileAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestBody UpdateOwnerProfileRequestDto updateOwnerProfileRequestDto
            ) {
        System.out.println("bearerJwtToken = " + bearerJwtToken);
        System.out.println("updateOwnerProfileRequestDto = " + updateOwnerProfileRequestDto);

    }
}
