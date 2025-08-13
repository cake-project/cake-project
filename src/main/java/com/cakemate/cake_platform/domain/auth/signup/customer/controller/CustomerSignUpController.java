package com.cakemate.cake_platform.domain.auth.signup.customer.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.OauthKakao.response.KakaoUserResponse;
import com.cakemate.cake_platform.domain.auth.signin.customer.dto.response.CustomerSignInResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.request.CustomerSignUpRequest;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.response.CustomerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.service.CustomerSignUpService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;

@Slf4j
@RestController
@RequestMapping("/api")
public class CustomerSignUpController {
    private final CustomerSignUpService customerSignUpService;

    public CustomerSignUpController(CustomerSignUpService customerSignUpService) {
        this.customerSignUpService = customerSignUpService;
    }

    @PostMapping("/customers/signup")
    public ApiResponse<?> customerSignUp(@Valid @RequestBody CustomerSignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String passwordConfirm = signUpRequest.getPasswordConfirm();
        String name = signUpRequest.getName();
        String phoneNumber = signUpRequest.getPhoneNumber();
        SearchCommand searchSignUpRequest = new SearchCommand(email, password, passwordConfirm, name, phoneNumber);

        ApiResponse<?> customerSignUpByLocal
                = customerSignUpService.customerLocalSignUpProcess(searchSignUpRequest);
        return customerSignUpByLocal;
    }
}