package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProposalFormUpdateRequestDto {
    //속성
    private Long proposalFormId;
    private String title;
//    private String cakeSize;
//    private int quantity;
    private String content;
    private String managerName;
    private int proposedPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime proposedPickupDate;

    private String image;
    private String proposalFormStatus;

    //생성자
    public ProposalFormUpdateRequestDto(Long proposalFormId, String title, String content, String managerName, int proposedPrice, LocalDateTime proposedPickupDate, String image, String proposalFormStatus) {
        this.proposalFormId = proposalFormId;
        this.title = title;
        this.content = content;
        this.managerName = managerName;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.image = image;
        this.proposalFormStatus = proposalFormStatus;
    }

    //기능
}
