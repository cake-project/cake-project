package com.cakemate.cake_platform.domain.auth.signup.owner.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signup.owner.dto.request.OwnerSignUpRequest;
import com.cakemate.cake_platform.domain.auth.signup.owner.dto.response.OwnerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.signup.owner.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("signup/owners")
    public ApiResponse<OwnerSignUpResponse> ownerSignUpApi(@Valid @RequestBody OwnerSignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String passwordConfirm = signUpRequest.getPasswordConfirm();
        String name = signUpRequest.getName();
        String phoneNumber = signUpRequest.getPhoneNumber();

        SearchCommand searchSignUpRequest = new SearchCommand(email, password, passwordConfirm, name, phoneNumber);

        ApiResponse<OwnerSignUpResponse> ownerSignUpSuccess = ownerService.ownerSaveProcess(searchSignUpRequest);
        return ownerSignUpSuccess;

    }
}
