package com.cakemate.cake_platform.domain.auth.signup.owner.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.request.CustomerSignUpRequest;
import com.cakemate.cake_platform.domain.auth.signup.owner.dto.request.OwnerSignUpRequest;
import com.cakemate.cake_platform.domain.auth.signup.owner.dto.response.OwnerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.signup.owner.service.OwnerSignUpService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;

@Slf4j
@RestController
@RequestMapping("/api")
public class OwnerSignUpController {
    private final OwnerSignUpService ownerSignUpService;

    public OwnerSignUpController(OwnerSignUpService ownerSignUpService) {
        this.ownerSignUpService = ownerSignUpService;
    }

    @PostMapping("/owners/signup")
    public ApiResponse<?> customerSignUp(@Valid @RequestBody CustomerSignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String passwordConfirm = signUpRequest.getPasswordConfirm();
        String name = signUpRequest.getName();
        String phoneNumber = signUpRequest.getPhoneNumber();

        SearchCommand searchSignUpRequest = new SearchCommand(email, password, passwordConfirm, name, phoneNumber);

        ApiResponse<?> ownerSignUpSuccess = ownerSignUpService.ownerLocalSignUpProcess(searchSignUpRequest);
        return ownerSignUpSuccess;
    }
}
