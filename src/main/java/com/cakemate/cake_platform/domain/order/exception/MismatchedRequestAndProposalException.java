package com.cakemate.cake_platform.domain.order.exception;

public class MismatchedRequestAndProposalException extends RuntimeException {
    public MismatchedRequestAndProposalException(String message) {
        super(message);
    }
}
