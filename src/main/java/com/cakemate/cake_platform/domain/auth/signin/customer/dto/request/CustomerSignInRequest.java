package com.cakemate.cake_platform.domain.auth.signin.customer.dto.request;

import lombok.Getter;

@Getter
public class CustomerSignInRequest {
    private final String email;
    private final String password;

    public CustomerSignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
