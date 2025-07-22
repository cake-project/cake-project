package com.cakemate.cake_platform.domain.proposalForm.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormContainsRequestFormDataDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormCreateRequestDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormDataDto;
import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class ProposalFormController {

    private final ProposalFormService proposalFormService;

    public ProposalFormController(ProposalFormService proposalFormService) {
        this.proposalFormService = proposalFormService;
    }

    /**
     * proposalForm 생성 API
     */
    @PostMapping
    public ApiResponse<ProposalFormDataDto> createProposalFormAPI(@RequestBody ProposalFormCreateRequestDto requestDto) {
        ApiResponse<ProposalFormDataDto> response = proposalFormService.createProposal(requestDto);
        return response;
    }
    /**
     * proposalForm 단건 상세 조회 API
     */
    @GetMapping("/proposalForms/{proposalFormId}")
    public ApiResponse<ProposalFormContainsRequestFormDataDto> getProposalFormDetailAPI(@PathVariable("proposalFormId") Long proposalFormId) {
        ApiResponse<ProposalFormContainsRequestFormDataDto> response = proposalFormService.getProposalFormDetail(proposalFormId);
        return response;
    }

    /**
     * proposalForm 목록 조회 API
     */
    @GetMapping("/{ownerId}/proposalForms")
    public ApiResponse<List<ProposalFormContainsRequestFormDataDto>> getProposalFormsAPI(@PathVariable("ownerId") Long ownerId) {
        ApiResponse<List<ProposalFormContainsRequestFormDataDto>> response = proposalFormService.getProposalFormList();
        return response;
    }
}
