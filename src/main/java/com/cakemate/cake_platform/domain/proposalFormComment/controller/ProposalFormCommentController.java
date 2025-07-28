package com.cakemate.cake_platform.domain.proposalFormComment.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormContainsRequestFormDataDto;
import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.request.CommentCreateRequestDto;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.response.CommentCreateResponseDto;
import com.cakemate.cake_platform.domain.proposalFormComment.service.ProposalFormCommentService;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProposalFormCommentController {

    private final ProposalFormService proposalFormService;
    private final ProposalFormCommentService proposalFormCommentService;
    private final JwtUtil jwtUtil;

    public ProposalFormCommentController(ProposalFormService proposalFormService, ProposalFormCommentService proposalFormCommentService, JwtUtil jwtUtil) {
        this.proposalFormService = proposalFormService;
        this.proposalFormCommentService = proposalFormCommentService;
        this.jwtUtil = jwtUtil;
    }


    /**
     *  댓글 생성 API
     */
    @GetMapping("/proposalForms/{proposalFormId}/comments")
    public ApiResponse<CommentCreateResponseDto> createProposalFormComment(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable("proposalFormId") Long proposalFormId,
            @RequestBody CommentCreateRequestDto commentCreateRequestDto
            ) {
        // 토큰에서 memberId 추출
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long memberId = jwtUtil.subjectMemberId(claims);

        return proposalFormCommentService.createRequestFormCommentService(
                commentCreateRequestDto, proposalFormId, memberId
                );
    }


    /**
     * proposalForm(소비자) 단건 상세 조회 API
     */
    @GetMapping("customer/proposalForms/{proposalFormId}")
    public ApiResponse<ProposalFormContainsRequestFormDataDto> getProposalFormDetailAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable("proposalFormId") Long proposalFormId
    ) {
        // 토큰 파싱 및 인증 처리

        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);
        return proposalFormService.getProposalFormDetail(proposalFormId);
    }
}
