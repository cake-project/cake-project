package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.cakemate.cake_platform.domain.proposalFormChat.dto.ChatHistorySectionDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CustomerProposalFormDetailDto {
    //속성
    private RequestFormDataDto requestForm;
    private CustomerProposalFormDataDto proposalForm;
    private List<CommentDataDto> comments;
    private final ChatHistorySectionDto chat;

    //생성자
    public CustomerProposalFormDetailDto(RequestFormDataDto requestForm, CustomerProposalFormDataDto proposalForm, ChatHistorySectionDto chat, List<CommentDataDto> comments) {
        this.requestForm = requestForm;
        this.proposalForm = proposalForm;
        this.comments = comments;
        this.chat = chat;
    }

    //기능
}
