package com.cakemate.cake_platform.domain.proposalFormComment.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.request.CommentCreateRequestDto;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.response.CommentCreateResponseDto;
import com.cakemate.cake_platform.domain.proposalFormComment.service.ProposalFormCommentService;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/proposal-forms/{proposalFormId}/comments")
    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> createProposalFormComment(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable("proposalFormId") Long proposalFormId,
            @RequestBody CommentCreateRequestDto commentCreateRequestDto
    ) {
        ApiResponse<CommentCreateResponseDto> requestFormCommentService
                = proposalFormCommentService.createRequestFormCommentService(
                        bearerJwtToken, commentCreateRequestDto, proposalFormId
        );
        ResponseEntity<ApiResponse<CommentCreateResponseDto>> response
                = ResponseEntity.ok(requestFormCommentService);
        return response;
    }


}
