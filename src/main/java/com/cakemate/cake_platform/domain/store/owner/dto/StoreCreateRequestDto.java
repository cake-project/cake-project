package com.cakemate.cake_platform.domain.store.owner.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;

public class StoreCreateRequestDto {
    private  Long ownerId;
    private String name;
    private String address;
    private String businessNumber;
    private String phoneNumber;
    private String image;
    private boolean isActive;

    //기본생성자
    public StoreCreateRequestDto() {

    }

    public StoreCreateRequestDto(Store store) {
        this.ownerId = store.getOwner().getId(); // Long 타입 ownerId로 저장
        this.name = store.getName();
        this.address = store.getAddress();
        this.businessNumber = store.getBusinessNumber();
        this.phoneNumber = store.getPhoneNumber();
        this.image = store.getImage();
        this.isActive = store.isActive();
    }

    public Long getOwnerId() {
        return ownerId;
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

    public boolean isActive() {
        return isActive;
    }
}
