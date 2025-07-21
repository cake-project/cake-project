package com.cakemate.cake_platform.domain.auth.signup.owner.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.domain.auth.signup.owner.dto.request.OwnerSignUpRequest;
import com.cakemate.cake_platform.domain.auth.signup.owner.service.OwnerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/owners")
    public void ownerSignUpApi(OwnerSignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String passwordConfirm = signUpRequest.getPasswordConfirm();
        String name = signUpRequest.getName();
        String phoneNumber = signUpRequest.getPhoneNumber();

        SearchCommand searchSignUpRequest = new SearchCommand(email, password, passwordConfirm, name, phoneNumber);

    }
}
