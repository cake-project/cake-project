package com.cakemate.cake_platform.domain.order.exception;

public class UnauthorizedRequestFormAccessException extends RuntimeException {
    public UnauthorizedRequestFormAccessException(String message) {
        super(message);
    }
}
