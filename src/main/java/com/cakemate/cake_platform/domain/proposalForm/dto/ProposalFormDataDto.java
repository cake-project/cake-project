package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * proposalFormCreateResponseDto에서 활용하는 객체입니다!!
 */
public class ProposalFormDataDto {
    //속성
    private Long id;
    private String title;
    private String content;
    private String managerName;
    private int proposedPrice;
    private LocalDateTime proposedPickupDate;
    private LocalDateTime createdAt;
    private String status;
    private String image;

    //owner 전용 생성자(모든 정보 다룸)
    public ProposalFormDataDto(Long id, String title, String content, String managerName, int proposedPrice, LocalDateTime proposedPickupDate, LocalDateTime createdAt, String status, String image) {
        this.id = id;
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
    public ProposalFormDataDto(Long id, String title, String content, int proposedPrice, LocalDateTime proposedPickupDate, LocalDateTime createdAt, String status, String image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.createdAt = createdAt;
        this.status = status;
        this.image = image;
    }

    //게터
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getProposedPrice() {
        return proposedPrice;
    }

    public LocalDateTime getProposedPickupDate() {
        return proposedPickupDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getManagerName() {
        return managerName;
    }
}

