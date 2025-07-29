package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonToken;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * proposalFormCreateResponseDto에서 활용하는 객체입니다!!
 */
@Getter
public class ProposalFormDataDto {
    //속성
    private Long proposalFormId;
    private Long requestFormId;
    private String storeName;
    private String title;
    private String content;
    private String managerName;
    private int proposedPrice;
    private LocalDateTime proposedPickupDate;
    private LocalDateTime createdAt;
    private String status;
    private String image;

    //owner 전용 생성자(모든 정보 다룸)
    public ProposalFormDataDto(Long proposalFormId, Long requestFormId, String storeName, String title, String content, String managerName, int proposedPrice, LocalDateTime proposedPickupDate, LocalDateTime createdAt, String status, String image) {
        this.proposalFormId = proposalFormId;
        this.requestFormId = requestFormId;
        this.storeName = storeName;
        this.title = title;
        this.content = content;
        this.managerName = managerName;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.createdAt = createdAt;
        this.status = status;
        this.image = image;
    }

    //customer 전용 생성자(담당자 정보 제외)
    public ProposalFormDataDto(Long proposalFormId, Long requestFormId, String storeName, String title, String content, int proposedPrice, LocalDateTime proposedPickupDate, LocalDateTime createdAt, String status, String image) {
        this.proposalFormId = proposalFormId;
        this.requestFormId = requestFormId;
        this.storeName = storeName;
        this.title = title;
        this.content = content;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.createdAt = createdAt;
        this.status = status;
        this.image = image;
    }

    //기능
}

