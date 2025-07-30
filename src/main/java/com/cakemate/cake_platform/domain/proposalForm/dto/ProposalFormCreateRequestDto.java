package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProposalFormCreateRequestDto {
    //속성
    private Long requestFormId;
    private String title;
//    private String cakeSize;
//    private int quantity;
    private String content;
    private String managerName;
    private int proposedPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime proposedPickupDate;

    private String image;

    //생성자
    public ProposalFormCreateRequestDto() {
    }

    public ProposalFormCreateRequestDto(Long requestFormId, String title, String content, String managerName, int proposedPrice, LocalDateTime proposedPickupDate, String image) {
        this.requestFormId = requestFormId;
        this.title = title;
        this.content = content;
        this.managerName = managerName;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.image = image;
    }

    //기능

}

