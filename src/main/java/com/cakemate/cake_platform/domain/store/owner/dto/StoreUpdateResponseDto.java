package com.cakemate.cake_platform.domain.store.owner.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class StoreUpdateResponseDto {
    private Long id;
    private String name;
    private String businessName;
    private String address;
    private String phoneNumber;
    private String image;
    private boolean isActive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedAt;

    //기본 생성자
    public StoreUpdateResponseDto () {}

    public StoreUpdateResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.businessName = store.getBusinessName();
        this.address = store.getAddress();
        this.phoneNumber = store.getPhoneNumber();
        this.image = store.getImage();
        this.isActive = store.isActive();
    }
}
