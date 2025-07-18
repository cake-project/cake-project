package com.cakemate.cake_platform.domain.requestForm.owner.controller;

import com.cakemate.cake_platform.domain.requestForm.owner.service.RequestFormOwnerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owners")
public class RequestFormOwnerController {

    private final RequestFormOwnerService requestFormOwnerService;

    public RequestFormOwnerController(RequestFormOwnerService requestFormOwnerService) {
        this.requestFormOwnerService = requestFormOwnerService;
    }
}
