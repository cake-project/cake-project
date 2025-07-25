package com.cakemate.cake_platform.domain.proposalForm.dto;

import java.util.List;

/**
 * proposalFormDetailResponseDto, proposalFormListResponseDto에서 활용하는 객체입니다!!
 * 단건 상세 조회, 목록 조회 모두 사용
 */
public class ProposalFormContainsRequestFormDataDto {
    //속성
    private RequestFormDataDto requestForm;
    private ProposalFormDataDto proposalForm;
    private List<CommentDataDto> comments;

    //생성자
    public ProposalFormContainsRequestFormDataDto(RequestFormDataDto requestForm, ProposalFormDataDto proposalForm, List<CommentDataDto> comments) {
        this.requestForm = requestForm;
        this.proposalForm = proposalForm;
        this.comments = comments;
    }

    // 목록 조회용 생성자 (commentList 없이)
    public ProposalFormContainsRequestFormDataDto(RequestFormDataDto requestForm, ProposalFormDataDto proposalForm) {
        this.requestForm = requestForm;
        this.proposalForm = proposalForm;
    }

    //게터
    public RequestFormDataDto getRequestForm() { return requestForm; }
    public ProposalFormDataDto getProposalForm() { return proposalForm; }
    public List<CommentDataDto> getComments() { return comments; }
}