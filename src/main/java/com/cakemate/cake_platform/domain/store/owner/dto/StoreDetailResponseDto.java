package com.cakemate.cake_platform.domain.store.owner.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class StoreDetailResponseDto {
    private Long id;
    private String name;
    private String address;
    private String businessNumber;
    private String phoneNumber;
    private String image;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private boolean isActive;

    //기본 생성자
    public StoreDetailResponseDto() {}

    public StoreDetailResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.businessNumber = store.getBusinessNumber();
        this.phoneNumber = store.getPhoneNumber();
        this.image = store.getImage();
        this.createdAt = store.getCreateAt();
        this.isActive = store.isActive();
    }
}
