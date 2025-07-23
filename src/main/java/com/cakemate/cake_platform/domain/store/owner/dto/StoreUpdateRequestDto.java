package com.cakemate.cake_platform.domain.store.owner.dto;



import lombok.Getter;


@Getter
public class StoreUpdateRequestDto {

    private Long id;
    private String name;
    private String businessName;
    private String address;
    private String phoneNumber;
    private String image;
    private Boolean isActive;

}
