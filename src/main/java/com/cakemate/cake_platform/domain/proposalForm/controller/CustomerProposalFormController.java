package com.cakemate.cake_platform.domain.proposalForm.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.proposalForm.dto.CustomerProposalFormDetailDto;
import com.cakemate.cake_platform.domain.proposalForm.service.CustomerProposalFormService;
import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/proposalForms")
public class CustomerProposalFormController {
    //속성
    private final ProposalFormService proposalFormService;
    private final CustomerProposalFormService customerProposalFormService;

    private final JwtUtil jwtUtil;

    //생성자
    public CustomerProposalFormController(ProposalFormService proposalFormService, CustomerProposalFormService customerProposalFormService,JwtUtil jwtUtil) {
        this.proposalFormService = proposalFormService;
        this.customerProposalFormService = customerProposalFormService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 소비자 전용 proposalForm 상세 조회 API
     */
    @GetMapping("/{proposalFormId}")
    public ApiResponse<CustomerProposalFormDetailDto> getCustomerProposalFormDetail(
            @PathVariable Long proposalFormId,
            @RequestParam Long customerId,
            @RequestHeader("Authorization") String bearerToken) {
        String token = jwtUtil.substringToken(bearerToken);
        Claims claims = jwtUtil.verifyToken(token);
        Long ownerId = jwtUtil.subjectMemberId(claims);
        ApiResponse<CustomerProposalFormDetailDto> response = customerProposalFormService.getProposalFormDetailForCustomer(proposalFormId, customerId);
        return response;
    }
}
