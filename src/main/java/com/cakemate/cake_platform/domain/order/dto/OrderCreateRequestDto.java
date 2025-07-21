package com.cakemate.cake_platform.domain.order.dto;

public class OrderCreateRequestDto {

    private Long requestFormId;
    private Long proposalFormId;

    public OrderCreateRequestDto(Long requestFormId, Long proposalFormId) {
        this.requestFormId = requestFormId;
        this.proposalFormId = proposalFormId;
    }

    public Long getRequestFormId() {
        return requestFormId;
    }

    public Long getProposalFormId() {
        return proposalFormId;
    }
}
