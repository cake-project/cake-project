package com.cakemate.cake_platform.common.exception;

public class MemberAlreadyDeletedException extends RuntimeException {
    public MemberAlreadyDeletedException(String message) {
        super(message);
    }
}
