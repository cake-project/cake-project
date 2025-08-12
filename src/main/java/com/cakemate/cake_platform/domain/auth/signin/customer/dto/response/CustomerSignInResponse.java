package com.cakemate.cake_platform.domain.auth.signin.customer.dto.response;

import lombok.Getter;

@Getter
public class CustomerSignInResponse {
    private final String jwtToken;

    // 추후 사용자의 접속한 지역에 따라 시간도 표기

    public CustomerSignInResponse(String jwtToken) {
        this.jwtToken = jwtToken;

    }
}
