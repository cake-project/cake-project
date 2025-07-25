package com.cakemate.cake_platform.domain.proposalForm.dto;

import java.util.List;

public class CustomerProposalFormDetailDto {
    //속성
    private ProposalFormDataDto proposalForm;
    private List<CommentDataDto> comments;

    //생성자
    public CustomerProposalFormDetailDto(ProposalFormDataDto proposalForm, List<CommentDataDto> comments) {
        this.proposalForm = proposalForm;
        this.comments = comments;
    }

    //게터
    public ProposalFormDataDto getProposalForm() {
        return proposalForm;
    }

    public List<CommentDataDto> getComments() {
        return comments;
    }
}
