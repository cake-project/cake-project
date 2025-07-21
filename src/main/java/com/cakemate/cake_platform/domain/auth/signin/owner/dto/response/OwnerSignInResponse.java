package com.cakemate.cake_platform.domain.auth.signin.owner.dto.response;

import lombok.Getter;

@Getter
public class OwnerSignInResponse {
    private final String jwtOwnerToken;
    // 추후 사용자의 접속한 지역에 따라 시간도 표기

    public OwnerSignInResponse(String jwtOwnerToken) {
        this.jwtOwnerToken = jwtOwnerToken;
    }
}
