package com.cakemate.cake_platform.domain.proposalFormComment.controller;

import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.request.CommentCreateRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProposalFormCommentController {

    private final ProposalFormService proposalFormService;

    public ProposalFormCommentController(ProposalFormService proposalFormService) {
        this.proposalFormService = proposalFormService;
    }

    @PostMapping("/proposalForms/{proposalFormId}/comments")
    public void createProposalFormComment(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable("proposalFormId") Long proposalFormId,
            @RequestBody CommentCreateRequestDto commentCreateRequestDto
            ) {
        System.out.println("bearerJwtToken = " + bearerJwtToken);
        System.out.println("proposalFormId = " + proposalFormId);
        System.out.println("commentCreateRequestDto = " + commentCreateRequestDto);

    }


}
