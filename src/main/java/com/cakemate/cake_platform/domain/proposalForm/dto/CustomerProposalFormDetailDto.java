package com.cakemate.cake_platform.domain.proposalForm.dto;

import java.util.List;

public class CustomerProposalFormDetailDto {
    //속성
    private RequestFormDataDto requestForm;
    private ProposalFormDataDto proposalForm;
    private List<CommentDataDto> comments;

    //생성자
    public CustomerProposalFormDetailDto(RequestFormDataDto requestForm, ProposalFormDataDto proposalForm, List<CommentDataDto> comments) {
        this.requestForm = requestForm;
        this.proposalForm = proposalForm;
        this.comments = comments;
    }

    //게터

    public RequestFormDataDto getRequestForm() {
        return requestForm;
    }

    public ProposalFormDataDto getProposalForm() {
        return proposalForm;
    }

    public List<CommentDataDto> getComments() {
        return comments;
    }
}
