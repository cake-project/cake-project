package com.cakemate.cake_platform.domain.auth.signup.customer.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.request.CustomerSignUpRequest;
import com.cakemate.cake_platform.domain.auth.signup.customer.entity.Customer;
import com.cakemate.cake_platform.domain.auth.signup.customer.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @PostMapping("/customers")
    public ApiResponse<Customer> customerSignUpApi(CustomerSignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String passwordConfirm = signUpRequest.getPasswordConfirm();
        String name = signUpRequest.getName();
        String phoneNumber = signUpRequest.getPhoneNumber();

        SearchCommand searchSignUpRequest = new SearchCommand(email, password, passwordConfirm, name, phoneNumber);

        ApiResponse<Customer> customerSignUp = customerService.customerSaveProcess(searchSignUpRequest);
        return customerSignUp;

    }
}
