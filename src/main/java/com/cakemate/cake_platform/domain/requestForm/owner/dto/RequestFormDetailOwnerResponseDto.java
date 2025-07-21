package com.cakemate.cake_platform.domain.requestForm.owner.dto;

import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RequestFormDetailOwnerResponseDto {

    private Long requestFormId;
    private String customerName;
    private String title;
    private String region;
    private String content;
    private int desiredPrice;
    private String image;
    private LocalDateTime pickupDate;
    private RequestFormStatus status;
    @JsonFormat
    private LocalDateTime createdAt;

    public RequestFormDetailOwnerResponseDto(Long requestFormId, String customerName, String title, String region, String content, int desiredPrice, String image, LocalDateTime pickupDate, RequestFormStatus status, LocalDateTime createdAt) {
        this.requestFormId = requestFormId;
        this.customerName = customerName;
        this.title = title;
        this.region = region;
        this.content = content;
        this.desiredPrice = desiredPrice;
        this.image = image;
        this.pickupDate = pickupDate;
        this.status = status;
        this.createdAt = createdAt;
    }
}
