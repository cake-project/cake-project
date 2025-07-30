package com.cakemate.cake_platform.domain.proposalForm.exception;

public class ProposalAlreadyAcceptedException extends RuntimeException {
    public ProposalAlreadyAcceptedException(String message) {
        super(message);
    }
}
