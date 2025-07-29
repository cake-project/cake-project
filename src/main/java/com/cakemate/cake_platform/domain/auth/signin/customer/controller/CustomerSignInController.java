package com.cakemate.cake_platform.domain.auth.signin.customer.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signin.customer.dto.request.CustomerSignInRequest;
import com.cakemate.cake_platform.domain.auth.signin.customer.dto.response.CustomerSignInResponse;
import com.cakemate.cake_platform.domain.auth.signin.customer.service.CustomerSignInService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CustomerSignInController {
    private final CustomerSignInService customerSignInService;

    public CustomerSignInController(CustomerSignInService customerSignInService) {
        this.customerSignInService = customerSignInService;
    }
    @PostMapping("/signin/customers")
    public ApiResponse<CustomerSignInResponse> CustomerSignInApi(@RequestBody CustomerSignInRequest customerSignInRequest) {
        String email = customerSignInRequest.getEmail();
        String password = customerSignInRequest.getPassword();

        SearchCommand signInRequest = new SearchCommand(email, password);

        ApiResponse<CustomerSignInResponse> customerSignInProcess = customerSignInService.CustomerSignInProcess(signInRequest);
        return customerSignInProcess;
    }

}
