package com.cakemate.cake_platform.domain.proposalForm.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class RequestFormDataDto {
    //속성
    private Long requestFormId;
    private String title;
    private String region;
    private String content;
    private int desiredPrice;
    private LocalDateTime desiredPickupDate;
    private String requestFormStatus;
    private LocalDateTime createdAt;
    private String image;

    //생성자
    public RequestFormDataDto(Long requestFormId, String title, String region, String content, Integer desiredPrice,
                              String image, LocalDateTime desiredPickupDate, String requestFormStatus,
                              LocalDateTime createdAt) {
        this.requestFormId = requestFormId;
        this.title = title;
        this.region = region;
        this.content = content;
        this.desiredPrice = desiredPrice;
        this.image = image;
        this.desiredPickupDate = desiredPickupDate;
        this.requestFormStatus = requestFormStatus;
        this.createdAt = createdAt;
    }

    //기능
}
