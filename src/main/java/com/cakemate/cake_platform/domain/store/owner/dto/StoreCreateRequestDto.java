package com.cakemate.cake_platform.domain.store.owner.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreCreateRequestDto {

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
