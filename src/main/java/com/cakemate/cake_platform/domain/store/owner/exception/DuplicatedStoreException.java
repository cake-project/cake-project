package com.cakemate.cake_platform.domain.store.owner.exception;

public class DuplicatedStoreException extends RuntimeException {
    public DuplicatedStoreException(String message) {
        super(message);
    }
}
