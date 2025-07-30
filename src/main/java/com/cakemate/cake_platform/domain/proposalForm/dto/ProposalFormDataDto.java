package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.cakemate.cake_platform.common.commonEnum.CakeSize;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * proposalFormCreateResponseDto, proposalFormListResponseDto에서 활용하는 객체입니다!!
 * 생성, 목록 조회 시 사용
 */
@Getter
public class ProposalFormDataDto {
    //속성
    private Long proposalFormId;
    private Long requestFormId;
    private String storeName;
    private String title;
    private CakeSize cakeSize;
    private int quantity;
    private String content;
    private String managerName;
    private int proposedPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime proposedPickupDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private String status;
    private String image;

    //owner 전용 생성자(모든 정보 다룸)
    public ProposalFormDataDto(Long proposalFormId, Long requestFormId, String storeName, String title, CakeSize cakeSize, int quantity, String content, String managerName, int proposedPrice, LocalDateTime proposedPickupDate, LocalDateTime createdAt, String status, String image) {
        this.proposalFormId = proposalFormId;
        this.requestFormId = requestFormId;
        this.storeName = storeName;
        this.title = title;
        this.cakeSize = cakeSize;
        this.quantity = quantity;
        this.content = content;
        this.managerName = managerName;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.createdAt = createdAt;
        this.status = status;
        this.image = image;
    }

    //기능
}

