package com.cakemate.cake_platform.domain.auth.signin.owner.controller;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signin.owner.dto.request.OwnerSignInRequest;
import com.cakemate.cake_platform.domain.auth.signin.owner.dto.response.OwnerSignInResponse;
import com.cakemate.cake_platform.domain.auth.signin.owner.service.OwnerSignInService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;

@RestController
@RequestMapping("/api")
public class OwnerSignInController {
    private final OwnerSignInService ownerSignInService;

    public OwnerSignInController(OwnerSignInService ownerSignInService) {
        this.ownerSignInService = ownerSignInService;
    }

    @GetMapping("/auth/owners/signin")
    public RedirectView authorize(@RequestParam(required = false) String scope) {
        RedirectView redirectView = new RedirectView(ownerSignInService.getAuthUrlKakao(scope));
        return redirectView;
    }

    @GetMapping("/owners/kakao/signin")
    public ApiResponse<?> handleKakaoCallback(@RequestParam String code) {
        ApiResponse<?> kakaoSignUpProcess = ownerSignInService.ownerKakaoSignInProcess(code);
        return kakaoSignUpProcess;
    }

    @PostMapping("/owners/signin")
    public ApiResponse<OwnerSignInResponse> OwnerSignInApi(@RequestBody OwnerSignInRequest ownerSignInRequest) {
        String email = ownerSignInRequest.getEmail();
        String password = ownerSignInRequest.getPassword();

        SearchCommand signInRequest = new SearchCommand(email, password);

        ApiResponse<OwnerSignInResponse> ownerSignInProcess = ownerSignInService.ownerSignInProcess(signInRequest);
        return ownerSignInProcess;
    }
}