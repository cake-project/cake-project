package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;

public class ProposalFormCreateRequestDto {
    //속성
    private Long requestFormId;
    private String title;
    private String content;
    private String proposalFormStatus;

    //생성자
    public ProposalFormCreateRequestDto() {
    }

    public ProposalFormCreateRequestDto(Long requestFormId, String title, String content, String proposalFormStatus) {
        this.requestFormId = requestFormId;
        this.title = title;
        this.content = content;
        this.proposalFormStatus = proposalFormStatus;
    }

    //기능
    public Long getRequestFormId() {
        return requestFormId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getProposalFormStatus() {
        return proposalFormStatus;
    }

    public ProposalFormStatus toEnumStatus() {
        return ProposalFormStatus.fromString(this.proposalFormStatus);
    }
}

