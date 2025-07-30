package com.cakemate.cake_platform.domain.order.customer.exception;

public class ProposalAlreadyOrderedException extends RuntimeException {
    public ProposalAlreadyOrderedException(String message) {
        super(message);
    }
}
