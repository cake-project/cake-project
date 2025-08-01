package com.cakemate.cake_platform.domain.member.owner.dto.request;

import lombok.Getter;

@Getter
public class UpdateOwnerProfileRequestDto {
    private String password;
    private String passwordConfirm;
    private String phoneNumber;

}
