package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ProposalFormCreateRequestDto {
    //속성
    private Long requestFormId;
    private String storeName;
    private String title;
    private String content;
    private int proposedPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime proposedPickupDate;

    private String image;
    private String proposalFormStatus;

    //생성자
    public ProposalFormCreateRequestDto() {
    }

    public ProposalFormCreateRequestDto(Long requestFormId, String storeName, String title, String content, int proposedPrice, LocalDateTime proposedPickupDate, String image, String proposalFormStatus) {
        this.requestFormId = requestFormId;
        this.storeName = storeName;
        this.title = title;
        this.content = content;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.image = image;
        this.proposalFormStatus = proposalFormStatus;
    }

    //게터
    public Long getRequestFormId() {
        return requestFormId;
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

    public String getImage() {
        return image;
    }

    public String getProposalFormStatus() {
        return proposalFormStatus;
    }

    public String getStoreName() {
        return storeName;
    }
}

