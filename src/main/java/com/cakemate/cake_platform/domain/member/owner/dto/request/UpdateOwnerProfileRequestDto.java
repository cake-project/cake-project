package com.cakemate.cake_platform.domain.member.owner.dto.request;

import lombok.Getter;

@Getter
public class UpdateOwnerProfileRequestDto {
    private String password;
    private String passwordConfirm;
    private String name;
    private String phoneNumber;

    public UpdateOwnerProfileRequestDto(String password, String passwordConfirm, String name, String phoneNumber) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
