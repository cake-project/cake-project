package com.cakemate.cake_platform.domain.auth.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("이미 Email이 존재 합니다. 로그인 해주세요");
    }
}
