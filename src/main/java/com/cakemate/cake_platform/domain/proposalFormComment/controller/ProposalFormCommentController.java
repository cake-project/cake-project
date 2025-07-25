package com.cakemate.cake_platform.domain.proposalFormComment.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.request.CommentCreateRequestDto;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.response.CommentCreateResponseDto;
import com.cakemate.cake_platform.domain.proposalFormComment.service.ProposalFormCommentService;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProposalFormCommentController {

    private final ProposalFormCommentService proposalFormCommentService;
    private final JwtUtil jwtUtil;

    public ProposalFormCommentController(ProposalFormCommentService proposalFormCommentService, JwtUtil jwtUtil) {
        this.proposalFormCommentService = proposalFormCommentService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/proposalForms/{proposalFormId}/comments")
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
}

