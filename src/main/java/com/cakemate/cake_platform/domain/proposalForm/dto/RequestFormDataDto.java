package com.cakemate.cake_platform.domain.proposalForm.dto;

import java.time.LocalDateTime;

public class RequestFormDataDto {
    //속성
    private Long id;
    private String title;
    private String region;
    private int desiredPrice;
    private LocalDateTime desiredPickupDate;
    private String status;
    private LocalDateTime createdAt;
    private String image;

    //생성자
    public RequestFormDataDto(Long id, String title, String region, int desiredPrice, LocalDateTime desiredPickupDate, String status, LocalDateTime createdAt, String image) {
        this.id = id;
        this.title = title;
        this.region = region;
        this.desiredPrice = desiredPrice;
        this.desiredPickupDate = desiredPickupDate;
        this.status = status;
        this.createdAt = createdAt;
        this.image = image;
    }

    //게터
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getRegion() {
        return region;
    }
    public int getDesiredPrice() { return desiredPrice; }
    public LocalDateTime getDesiredPickupDate() { return desiredPickupDate; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getImage() { return image; }
}
