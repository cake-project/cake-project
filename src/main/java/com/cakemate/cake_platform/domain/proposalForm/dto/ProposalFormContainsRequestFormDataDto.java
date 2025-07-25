package com.cakemate.cake_platform.domain.proposalForm.dto;

/**
 * proposalFormDetailResponseDto, proposalFormListResponseDto에서 활용하는 객체입니다!!
 * 단건 상세 조회, 목록 조회 모두 사용
 */
public class ProposalFormContainsRequestFormDataDto {
    //속성
    private ProposalFormDataDto proposalForm;
    private RequestFormDataDto requestForm;

    public ProposalFormContainsRequestFormDataDto(ProposalFormDataDto proposalForm, RequestFormDataDto requestForm) {
        this.proposalForm = proposalForm;
        this.requestForm = requestForm;
    }

    public ProposalFormDataDto getProposalForm() { return proposalForm; }
    public RequestFormDataDto getRequestForm() { return requestForm; }
}
