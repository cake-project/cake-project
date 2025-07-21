package com.cakemate.cake_platform.domain.store.customer.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StoreSummaryResponseDto {
    private Long storeId;
    private Long ownerId;
    private String storeName;
    private String storeAddress;
    private String businessNumber;
    private String storePhoneNumber;
    private String storeImage;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    private boolean isActive;

    //기본 생성자
    public StoreSummaryResponseDto() {}

    public StoreSummaryResponseDto(Store store) {
        this.storeId = store.getId();
        this.ownerId = store.getOwner().getId();
        this.storeName = store.getName();
        this.storeAddress = store.getAddress();
        this.businessNumber = store.getBusinessNumber();
        this.storePhoneNumber = store.getPhoneNumber();
        this.storeImage = store.getImage();
        this.createdAt = store.getCreatedAt();
        this.isActive = store.isActive();
    }
}
