package com.cakemate.cake_platform.domain.proposalForm.exception;

public class InvalidProposalStatusException extends RuntimeException {
    public InvalidProposalStatusException(String message) {
        super(message);
    }
}
