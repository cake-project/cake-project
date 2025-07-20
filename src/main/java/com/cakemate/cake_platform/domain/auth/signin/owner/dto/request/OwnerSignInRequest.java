package com.cakemate.cake_platform.domain.auth.signin.owner.dto.request;

import lombok.Getter;

@Getter
public class OwnerSignInRequest {
    private final String email;
    private final String password;

    public OwnerSignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
