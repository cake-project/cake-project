package com.cakemate.cake_platform.domain.store.customer.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StoreSummaryResponseDto {
    private Long id;
    private Long ownerId;
    private String name;
    private String address;
    private String businessNumber;
    private String phoneNumber;
    private String image;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    private boolean isActive;

    //기본 생성자
    public StoreSummaryResponseDto() {}

    public StoreSummaryResponseDto(Store store) {
        this.id = store.getId();
        this.ownerId = store.getOwner().getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.businessNumber = store.getBusinessNumber();
        this.phoneNumber = store.getPhoneNumber();
        this.image = store.getImage();
        this.createdAt = store.getCreatedAt();
        this.isActive = store.isActive();
    }
}
