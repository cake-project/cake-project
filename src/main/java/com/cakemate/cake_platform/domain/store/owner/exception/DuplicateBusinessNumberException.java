package com.cakemate.cake_platform.domain.store.owner.exception;

public class DuplicateBusinessNumberException extends RuntimeException {
    public DuplicateBusinessNumberException(String message) {
        super(message);
    }
}
