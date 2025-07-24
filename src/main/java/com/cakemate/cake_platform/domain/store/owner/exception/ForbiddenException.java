package com.cakemate.cake_platform.domain.store.owner.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
