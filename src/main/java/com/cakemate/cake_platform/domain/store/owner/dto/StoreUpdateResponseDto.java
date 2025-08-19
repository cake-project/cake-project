package com.cakemate.cake_platform.domain.store.owner.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class StoreUpdateResponseDto {
    private Long storeId;
    private String businessName;
    private String storeName;
    private String address;
    private String businessNumber;
    private String phoneNumber;
    private String image;
    private boolean isActive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    //기본 생성자
    public StoreUpdateResponseDto () {}

    public StoreUpdateResponseDto(Store store) {
        this.storeId = store.getId();
        this.businessName = store.getBusinessName();
        this.storeName = store.getName();
        this.address = store.getAddress();
        this.businessNumber = store.getBusinessNumber();
        this.phoneNumber = store.getPhoneNumber();
        this.image = store.getImage();
        this.isActive = store.isActive();
        this.createdAt = store.getCreatedAt();
        this.modifiedAt = store.getModifiedAt();
    }
}
