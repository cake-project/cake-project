package com.cakemate.cake_platform.domain.order.customer.exception;

public class MismatchedRequestAndProposalException extends RuntimeException {
    public MismatchedRequestAndProposalException(String message) {
        super(message);
    }
}
