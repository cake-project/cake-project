package com.cakemate.cake_platform.domain.auth.signup.owner.dto.request;

import com.cakemate.cake_platform.domain.auth.signup.owner.entity.Owner;
import lombok.Getter;

@Getter
public class OwnerSignUpRequest {
    private final String email;
    private final String password;
    private final String passwordConfirm;
    private final String name;
    private final String phoneNumber;

    public OwnerSignUpRequest(Owner owner) {
        this.email = owner.getEmail();
        this.password = owner.getPassword();
        this.passwordConfirm = owner.getPasswordConfirm();
        this.name = owner.getName();
        this.phoneNumber = owner.getPhoneNumber();
    }

}
