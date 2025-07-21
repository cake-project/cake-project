package com.cakemate.cake_platform.domain.proposalForm.controller;

import com.cakemate.cake_platform.domain.proposalForm.service.ProposalFormService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owners")
public class ProposalFormController {

    private final ProposalFormService proposalFormService;

    public ProposalFormController(ProposalFormService proposalFormService) {
        this.proposalFormService = proposalFormService;
    }
}
