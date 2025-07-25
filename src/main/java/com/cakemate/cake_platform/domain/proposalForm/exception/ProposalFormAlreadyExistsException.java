package com.cakemate.cake_platform.domain.proposalForm.exception;

public class ProposalFormAlreadyExistsException extends RuntimeException {
    public ProposalFormAlreadyExistsException(String message) {
        super(message);
    }
}
