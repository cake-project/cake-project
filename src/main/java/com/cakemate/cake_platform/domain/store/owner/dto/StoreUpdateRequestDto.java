package com.cakemate.cake_platform.domain.store.owner.dto;


import lombok.Getter;


@Getter
public class StoreUpdateRequestDto {

    private String name;
    private String address;
    private String phoneNumber;
    private String image;
    private Boolean isActive;

    //기본생성자
    public StoreUpdateRequestDto() {}
}


