package com.cakemate.cake_platform.domain.member.customer.dto.reponse;

import lombok.Getter;

@Getter
public class CustomerProfileResponseDto {
    private String name;
    private String email;
    private String phoneNumber;

    public CustomerProfileResponseDto(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
