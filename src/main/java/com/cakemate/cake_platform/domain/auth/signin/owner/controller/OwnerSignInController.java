package com.cakemate.cake_platform.domain.auth.signin.owner.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signin.owner.dto.request.OwnerSignInRequest;
import com.cakemate.cake_platform.domain.auth.signin.owner.dto.response.OwnerSignInResponse;
import com.cakemate.cake_platform.domain.auth.signin.owner.service.OwnerSignInService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OwnerSignInController {
    private final OwnerSignInService ownerSignInService;

    public OwnerSignInController(OwnerSignInService ownerSignInService) {
        this.ownerSignInService = ownerSignInService;
    }
    @PostMapping("/signin/owners")
    public ApiResponse<OwnerSignInResponse> OwnerSignInApi(@RequestBody OwnerSignInRequest ownerSignInRequest) {
        String email = ownerSignInRequest.getEmail();
        String password = ownerSignInRequest.getPassword();

        SearchCommand signInRequest = new SearchCommand(email, password);

        ApiResponse<OwnerSignInResponse> ownerSignInProcess = ownerSignInService.OwnerSignInProcess(signInRequest);
        return ownerSignInProcess;
    }
}
