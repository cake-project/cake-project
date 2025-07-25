package com.cakemate.cake_platform.domain.store.owner.exception;

public class AlreadyDeletedStoreException extends RuntimeException {
    public AlreadyDeletedStoreException(String message) {
        super(message);
    }
}
