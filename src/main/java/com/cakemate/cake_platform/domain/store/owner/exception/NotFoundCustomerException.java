package com.cakemate.cake_platform.domain.store.owner.exception;

public class NotFoundCustomerException extends RuntimeException {
    public NotFoundCustomerException(String message) {
        super(message);
    }
}
