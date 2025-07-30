package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerProposalFormConfirmRequestDto {

    private String proposalFormStatus;

    public ProposalFormStatus getProposalFormStatusEnum() {
        return ProposalFormStatus.fromString(proposalFormStatus);
    }
}
