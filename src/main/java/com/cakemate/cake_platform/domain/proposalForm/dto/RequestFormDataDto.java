package com.cakemate.cake_platform.domain.proposalForm.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public String getRegion() {
        return region;

    }
    public int getDesiredPrice() { return desiredPrice; }
    public LocalDateTime getDesiredPickupDate() { return desiredPickupDate; }

    public String getRequestFormStatus() {
        return requestFormStatus;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getImage() { return image; }

}
