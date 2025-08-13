package com.cakemate.cake_platform.domain.proposalForm.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.proposalForm.dto.*;
import com.cakemate.cake_platform.domain.proposalForm.service.CustomerProposalFormService;
import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import org.springframework.http.HttpStatus;
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
        Long ownerId = jwtUtil.extractOwnerId(bearerToken);
        ApiResponse<ProposalFormDataDto> response = proposalFormService.createProposal(ownerId, requestDto);
        return response;
    }

    /**
     * proposalForm 단건 상세 조회 API
     */
    @GetMapping("/{proposalFormId}")
    public ApiResponse<ProposalFormContainsRequestFormDataDto> getProposalFormDetailAPI(@PathVariable("proposalFormId") Long proposalFormId, @RequestHeader("Authorization") String bearerToken) {
        Long ownerId = jwtUtil.extractOwnerId(bearerToken);
        ApiResponse<ProposalFormContainsRequestFormDataDto> response = proposalFormService.getProposalFormDetail(proposalFormId, ownerId);
        return response;
    }

    /**
     * proposalForm 목록 조회 API
     */
    @GetMapping
    public ApiResponse<List<ProposalFormDataDto>> getProposalFormsAPI(@RequestHeader("Authorization") String bearerToken) {
        Long ownerId = jwtUtil.extractOwnerId(bearerToken);
        ApiResponse<List<ProposalFormDataDto>> response = proposalFormService.getProposalFormList(ownerId);
        return response;
    }

    /**
     * proposalForm 수정 API
     */
    @PutMapping("/{proposalFormId}")
    public ApiResponse<ProposalFormDataDto> updateProposalFormAPI(@PathVariable Long proposalFormId,
                                                                  @RequestBody ProposalFormUpdateRequestDto requestDto,
                                                                  @RequestHeader("Authorization") String bearerToken) {
        Long ownerId = jwtUtil.extractOwnerId(bearerToken);
        ApiResponse<ProposalFormDataDto> response = proposalFormService.updateProposalForm(proposalFormId, ownerId, requestDto);
        return response;
    }

    /**
     * proposalForm 삭제 API
     */
    @DeleteMapping("/{proposalFormId}")
    public ApiResponse<String> deleteProposalFormAPI(@PathVariable Long proposalFormId,
                                                     @RequestHeader("Authorization") String bearerToken) {
        Long ownerId = jwtUtil.extractOwnerId(bearerToken);
        ApiResponse<String> response = proposalFormService.deleteProposalForm(proposalFormId, ownerId);
        return response;
    }

    /**
     * proposalForm 최종 확정 API
     */
    @PatchMapping("{proposalFormId}/confirm")
    public ApiResponse<OwnerProposalFormConfirmResponseDto> updateProposalFormStatusAPI(@PathVariable Long proposalFormId,
                                                                                        @RequestHeader("Authorization") String bearerToken

    ) {
        Long ownerId = jwtUtil.extractOwnerId(bearerToken);
        OwnerProposalFormConfirmResponseDto responseDto = proposalFormService.confirmProposalForm(proposalFormId, ownerId);
        ApiResponse<OwnerProposalFormConfirmResponseDto> response = ApiResponse.success(HttpStatus.OK, "견적서 최종 확정이 완료되었습니다.", responseDto);
        return response;

    }
}