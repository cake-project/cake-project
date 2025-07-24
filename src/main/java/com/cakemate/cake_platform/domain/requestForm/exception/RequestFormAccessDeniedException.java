package com.cakemate.cake_platform.domain.requestForm.exception;

public class RequestFormAccessDeniedException extends RuntimeException {
    public RequestFormAccessDeniedException(String message) {
        super(message);
    }
}
