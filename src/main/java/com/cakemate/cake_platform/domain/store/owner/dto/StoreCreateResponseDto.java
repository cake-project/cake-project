package com.cakemate.cake_platform.domain.store.owner.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;

import java.time.LocalDateTime;

public class StoreCreateResponseDto {

    private Long id;
    private String name;
    private String address;
    private String businessNumber;
    private String phoneNumber;
    private String image;
    private LocalDateTime createdAt;
    private boolean isActive;

    public StoreCreateResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.businessNumber = store.getBusinessNumber();
        this.phoneNumber = store.getPhoneNumber();
        this.image = store.getImage();
        this.createdAt = store.getCreateAt();
        this.isActive = store.isActive();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }
}
