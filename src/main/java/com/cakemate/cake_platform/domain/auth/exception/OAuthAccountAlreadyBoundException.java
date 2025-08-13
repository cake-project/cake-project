package com.cakemate.cake_platform.domain.auth.exception;

public class OAuthAccountAlreadyBoundException extends RuntimeException {
    public OAuthAccountAlreadyBoundException(String OAuthName) {
        super("이미 " + OAuthName + "로 등록된 계정입니다. "
                + OAuthName + "로 로그인하기를 클릭해 주세요");
    }
}

