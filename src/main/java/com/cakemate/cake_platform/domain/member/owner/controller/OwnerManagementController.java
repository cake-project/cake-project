package com.cakemate.cake_platform.domain.member.owner.controller;

import com.cakemate.cake_platform.domain.member.owner.service.OwnerManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owners")
public class OwnerManagementController {

    private final OwnerManagementService ownerManagementService;

    public OwnerManagementController(OwnerManagementService ownerManagementService) {
        this.ownerManagementService = ownerManagementService;
    }
}
