package com.cakemate.cake_platform.domain.auth.exception;

public class InvalidPasswordFormatException extends RuntimeException {
    public InvalidPasswordFormatException(String message) {
        super(message);
    }
}
