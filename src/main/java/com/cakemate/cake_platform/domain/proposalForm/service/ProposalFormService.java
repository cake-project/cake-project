package com.cakemate.cake_platform.domain.proposalForm.service;

import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import org.springframework.stereotype.Service;

@Service
public class ProposalFormService {

    private final ProposalFormRepository proposalFormRepository;

    public ProposalFormService(ProposalFormRepository proposalFormRepository) {
        this.proposalFormRepository = proposalFormRepository;
    }
}
