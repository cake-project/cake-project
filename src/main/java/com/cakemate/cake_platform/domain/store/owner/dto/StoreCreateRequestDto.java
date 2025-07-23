package com.cakemate.cake_platform.domain.store.owner.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;
import lombok.Getter;

@Getter
public class StoreCreateRequestDto {
    private Long ownerId;
    private String businessName;
    private String name;
    private String address;
    private String businessNumber;
    private String phoneNumber;
    private String image;
    private boolean isActive;

    //기본생성자
    public StoreCreateRequestDto() {

    }


}
