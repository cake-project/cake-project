package com.cakemate.cake_platform.domain.auth.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("존재하지 않는 소비자 입니다.");
    }
}