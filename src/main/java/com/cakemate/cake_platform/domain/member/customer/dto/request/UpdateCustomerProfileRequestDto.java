package com.cakemate.cake_platform.domain.member.customer.dto.request;

import lombok.Getter;

@Getter
public class UpdateCustomerProfileRequestDto {
    private String password;
    private String passwordConfirm;
    private String phoneNumber;
}
