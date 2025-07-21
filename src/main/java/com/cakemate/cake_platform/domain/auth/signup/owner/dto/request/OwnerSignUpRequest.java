package com.cakemate.cake_platform.domain.auth.signup.owner.dto.request;

import lombok.Getter;

@Getter
public class OwnerSignUpRequest {
    private final String email;
    private final String password;
    private final String passwordConfirm;
    private final String name;
    private final String phoneNumber;

    public OwnerSignUpRequest(String email, String password, String passwordConfirm, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
