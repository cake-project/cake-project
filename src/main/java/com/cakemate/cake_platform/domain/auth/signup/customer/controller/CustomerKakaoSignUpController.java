package com.cakemate.cake_platform.domain.auth.signup.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.response.CustomerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.service.CustomerSignUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

@Slf4j
@RestController
public class CustomerKakaoSignUpController {
    private final CustomerSignUpService customerSignUpService;

    public CustomerKakaoSignUpController(CustomerSignUpService customerSignUpService) {
        this.customerSignUpService = customerSignUpService;
    }

    @GetMapping("/authorize")
    public RedirectView authorize(@RequestParam(required = false) String scope) {
        System.out.println("컨트롤러 RedirectView authorize 시작");
        log.info("scope {} ", scope);

        RedirectView redirectView = new RedirectView(customerSignUpService.getAuthUrl(scope));

        log.info("authorize redirectView {} ", redirectView);
        System.out.println("컨트롤러 RedirectView authorize 끝");
        return redirectView;
    }

    @GetMapping("/redirect")
    public ApiResponse<CustomerSignUpResponse> handleRedirect(@RequestParam String code) {
        System.out.println("컨트롤러 RedirectView handleRedirect 시작");
        log.info("code {} ", code);

        ApiResponse<CustomerSignUpResponse> kakaoUserCustomerSignUpSuccess
                = customerSignUpService.kakaoCustomerSaveProcess(code);

        return kakaoUserCustomerSignUpSuccess;
    }

}
