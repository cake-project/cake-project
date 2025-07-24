package com.cakemate.cake_platform.domain.store.owner.exception;

public class MissingStoreIdException extends RuntimeException {
    public MissingStoreIdException(String message) {
        super(message);
    }
}
