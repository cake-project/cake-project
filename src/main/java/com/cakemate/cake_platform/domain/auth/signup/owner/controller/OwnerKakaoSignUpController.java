package com.cakemate.cake_platform.domain.auth.signup.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.response.CustomerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.service.CustomerSignUpService;
import com.cakemate.cake_platform.domain.auth.signup.owner.service.OwnerSignUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

@Slf4j
@RestController
public class OwnerKakaoSignUpController {
    private final OwnerSignUpService ownerSignUpService;

    public OwnerKakaoSignUpController(OwnerSignUpService ownerSignUpService) {
        this.ownerSignUpService = ownerSignUpService;
    }

    @GetMapping("/authorize")
    public RedirectView authorize(@RequestParam(required = false) String scope) {
        System.out.println("컨트롤러 RedirectView authorize 시작");
        log.info("scope {} ", scope);

        RedirectView redirectView = new RedirectView(ownerSignUpService.getAuthUrl(scope));

        log.info("authorize redirectView {} ", redirectView);
        System.out.println("컨트롤러 RedirectView authorize 끝");
        return redirectView;
    }

    @GetMapping("/redirect")
    public ApiResponse<CustomerSignUpResponse> handleRedirect(@RequestParam String code) {
        System.out.println("컨트롤러 RedirectView handleRedirect 시작");
        log.info("code {} ", code);

        ApiResponse<CustomerSignUpResponse> kakaoUserCustomerSignUpSuccess
                = ownerSignUpService.kakaoCustomerSaveProcess(code);

        return kakaoUserCustomerSignUpSuccess;
    }

}
