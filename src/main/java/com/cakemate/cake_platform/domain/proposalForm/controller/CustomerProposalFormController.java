package com.cakemate.cake_platform.domain.proposalForm.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.proposalForm.dto.CustomerProposalFormAcceptRequestDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.CustomerProposalFormAcceptResponseDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.CustomerProposalFormDetailDto;
import com.cakemate.cake_platform.domain.proposalForm.service.CustomerProposalFormService;
import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/proposalForms")
public class CustomerProposalFormController {
    //속성
    private final ProposalFormService proposalFormService;
    private final CustomerProposalFormService customerProposalFormService;
    private final JwtUtil jwtUtil;

    //생성자
    public CustomerProposalFormController(ProposalFormService proposalFormService, CustomerProposalFormService customerProposalFormService, JwtUtil jwtUtil) {
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
            @RequestHeader("Authorization") String bearerToken) {
        Long customerId = jwtUtil.extractCustomerId(bearerToken);
        ApiResponse<CustomerProposalFormDetailDto> response = customerProposalFormService.getProposalFormDetailForCustomer(proposalFormId, customerId);
        return response;
    }

    /**
     * 소비자 -> 견적서 선택 API
     */
    @PatchMapping("/{proposalFormId}/accept")
    public ApiResponse<CustomerProposalFormAcceptResponseDto> acceptProposalFormByCustomer(
            @PathVariable Long proposalFormId,
            @RequestBody CustomerProposalFormAcceptRequestDto requestDto,
            @RequestHeader("Authorization") String bearerToken
    ) {
        Long customerId = jwtUtil.extractCustomerId(bearerToken);

        CustomerProposalFormAcceptResponseDto responseDto = customerProposalFormService.acceptProposalFormByCustomer(proposalFormId, customerId, requestDto);
        ApiResponse<CustomerProposalFormAcceptResponseDto> response = ApiResponse.success(HttpStatus.OK, "견적서 선택이 완료되었습니다.", responseDto);
        return response;
    }
}
