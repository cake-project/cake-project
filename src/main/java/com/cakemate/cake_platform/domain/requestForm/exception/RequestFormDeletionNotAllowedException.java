package com.cakemate.cake_platform.domain.requestForm.exception;

public class RequestFormDeletionNotAllowedException extends RuntimeException {
    public RequestFormDeletionNotAllowedException(String message) {
        super(message);
    }
}
