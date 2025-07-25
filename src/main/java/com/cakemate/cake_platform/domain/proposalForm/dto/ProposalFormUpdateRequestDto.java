package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ProposalFormUpdateRequestDto {
    //속성
    private String storeName;
    private String title;
    private String content;
    private String managerName;
    private int proposedPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime proposedPickupDate;

    private String image;
    private String proposalFormStatus;

    //생성자
    public ProposalFormUpdateRequestDto(String storeName, String title, String content, String managerName, int proposedPrice, LocalDateTime proposedPickupDate, String image, String proposalFormStatus) {
        this.storeName = storeName;
        this.title = title;
        this.content = content;
        this.managerName = managerName;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.image = image;
        this.proposalFormStatus = proposalFormStatus;
    }

    //게터
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getManagerName() {
        return managerName;
    }

    public int getProposedPrice() {
        return proposedPrice;
    }

    public LocalDateTime getProposedPickupDate() {
        return proposedPickupDate;
    }

    public String getImage() {
        return image;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreName() {
        return storeName;
    }
}
