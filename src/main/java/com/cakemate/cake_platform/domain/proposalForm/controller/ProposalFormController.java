package com.cakemate.cake_platform.domain.proposalForm.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormContainsRequestFormDataDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormCreateRequestDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormDataDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormUpdateRequestDto;
import com.cakemate.cake_platform.domain.proposalForm.service.CustomerProposalFormService;
import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/proposalForms")
public class ProposalFormController {
    //속성
    private final ProposalFormService proposalFormService;
    private final CustomerProposalFormService proposalFormCustomerService;
    private final JwtUtil jwtUtil;

    //생성자
    public ProposalFormController(ProposalFormService proposalFormService, CustomerProposalFormService proposalFormCustomerService, JwtUtil jwtUtil) {
        this.proposalFormService = proposalFormService;
        this.proposalFormCustomerService = proposalFormCustomerService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * proposalForm 생성 API
     */
    @PostMapping
    public ApiResponse<ProposalFormDataDto> createProposalFormAPI(@RequestHeader("Authorization") String bearerToken, @RequestBody ProposalFormCreateRequestDto requestDto) {
        String token = jwtUtil.substringToken(bearerToken);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);

        System.out.println("ownerId = " + ownerId);  // << 이 부분 꼭 확인

        ApiResponse<ProposalFormDataDto> response = proposalFormService.createProposal(ownerId, requestDto);
        return response;
    }

    /**
     * proposalForm 단건 상세 조회 API
     */
    @GetMapping("/{proposalFormId}")
    public ApiResponse<ProposalFormContainsRequestFormDataDto> getProposalFormDetailAPI(
            @PathVariable("proposalFormId") Long proposalFormId,
            @RequestHeader("Authorization") String bearerToken
    ) {
        String token = jwtUtil.substringToken(bearerToken);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);
        ApiResponse<ProposalFormContainsRequestFormDataDto> response = proposalFormService.getProposalFormDetail(proposalFormId);
        return response;
    }

    /**
     * proposalForm 목록 조회 API
     */
    @GetMapping
    public ApiResponse<List<ProposalFormContainsRequestFormDataDto>> getProposalFormsAPI(@RequestHeader("Authorization") String bearerToken) {
        String token = jwtUtil.substringToken(bearerToken);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);
        ApiResponse<List<ProposalFormContainsRequestFormDataDto>> response = proposalFormService.getProposalFormList();
        return response;
    }

    /**
     * proposalForm 수정 API
     */
    @PutMapping("/{proposalFormId}")
    public ApiResponse<ProposalFormDataDto> updateProposalFormAPI(@PathVariable Long proposalFormId,
                                                                  @RequestBody ProposalFormUpdateRequestDto requestDto,
                                                                  @RequestHeader("Authorization") String bearerToken) {
        String token = jwtUtil.substringToken(bearerToken);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);
        ApiResponse<ProposalFormDataDto> response = proposalFormService.updateProposalForm(proposalFormId, ownerId, requestDto);
        return response;
    }

    /**
     * proposalForm 삭제 API
     */
    @DeleteMapping("/{proposalFormId}")
    public ApiResponse<String> deleteProposalFormAPI(@PathVariable Long proposalFormId,
                                                     @RequestHeader("Authorization") String bearerToken) {
        String token = jwtUtil.substringToken(bearerToken);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);
        ApiResponse<String> response = proposalFormService.deleteProposalForm(proposalFormId, ownerId);
        return response;
    }
}