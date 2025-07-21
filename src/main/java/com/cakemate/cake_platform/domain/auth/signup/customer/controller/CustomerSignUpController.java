package com.cakemate.cake_platform.domain.auth.signup.customer.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.request.CustomerSignUpRequest;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.response.CustomerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.service.CustomerSignUpService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CustomerSignUpController {
    private final CustomerSignUpService customerSignUpService;

    public CustomerSignUpController(CustomerSignUpService customerSignUpService) {
        this.customerSignUpService = customerSignUpService;
    }

    @PostMapping("/signup/customers")
    public ApiResponse<CustomerSignUpResponse> customerSignUpApi(@Valid @RequestBody CustomerSignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String passwordConfirm = signUpRequest.getPasswordConfirm();
        String name = signUpRequest.getName();
        String phoneNumber = signUpRequest.getPhoneNumber();

        SearchCommand searchSignUpRequest = new SearchCommand(email, password, passwordConfirm, name, phoneNumber);

        ApiResponse<CustomerSignUpResponse> customerSignUpSuccess
                = customerSignUpService.customerSaveProcess(searchSignUpRequest);
        return customerSignUpSuccess;

    }

}
