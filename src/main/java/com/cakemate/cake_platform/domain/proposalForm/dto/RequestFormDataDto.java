package com.cakemate.cake_platform.domain.proposalForm.dto;

import java.time.LocalDateTime;

public class RequestFormDataDto {
    //속성
    private Long id;
    private String title;
    private int desiredPrice;
    private LocalDateTime pickupDate;
    private String status;
    private LocalDateTime createdAt;
    private String image;

    //생성자
    public RequestFormDataDto(Long id, String title, int desiredPrice, LocalDateTime pickupDate, String status, LocalDateTime createdAt, String image) {
        this.id = id;
        this.title = title;
        this.desiredPrice = desiredPrice;
        this.pickupDate = pickupDate;
        this.status = status;
        this.createdAt = createdAt;
        this.image = image;
    }

    //게터
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public int getDesiredPrice() { return desiredPrice; }
    public LocalDateTime getPickupDate() { return pickupDate; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getImage() { return image; }
}
