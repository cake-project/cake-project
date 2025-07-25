package com.cakemate.cake_platform.domain.member.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.member.owner.dto.request.UpdateOwnerProfileRequestDto;
import com.cakemate.cake_platform.domain.member.owner.dto.response.OwnerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.owner.dto.response.UpdateOwnerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.owner.service.OwnerManagementService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * 점주 -> 내 정보 조회 API
     * @param bearerJwtToken
     * @return
     */
    @GetMapping("/me")
    public ApiResponse<OwnerProfileResponseDto> getOwnerProfileAPI(@RequestHeader("Authorization") String bearerJwtToken) {
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        OwnerProfileResponseDto responseDto = ownerManagementService.getOwnerProfileService(ownerId);
        ApiResponse<OwnerProfileResponseDto> response = ApiResponse.success(HttpStatus.OK, "회원 정보 조회가 완료되었습니다.", responseDto);
        return response;
    }

    /**
     * (점주) 내 정보 수정 API
     */
    @PutMapping("/me")
    public ApiResponse<UpdateOwnerProfileResponseDto> putOwnerProfileAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestBody UpdateOwnerProfileRequestDto updateOwnerProfileRequestDto
            ) {
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        return ownerManagementService.putUpdateOwnerService(
                ownerId, updateOwnerProfileRequestDto
        );
    }

    /**
     * 점주 회원 탈퇴
     */
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<OwnerProfileResponseDto>> deleteOwnerProfile(
            @RequestHeader("Authorization") String bearerJwtToken
    ) {
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        OwnerProfileResponseDto dto = ownerManagementService.deleteOwnerProfileService(ownerId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "점주 탈퇴 성공", dto));
    }
}
