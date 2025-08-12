package com.cakemate.cake_platform.domain.auth.signin.customer.controller;

import com.cakemate.cake_platform.domain.auth.signin.customer.service.CustomerSignInService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CustomerKakaoSignInController {
    private final CustomerSignInService customerSignInService;

    public CustomerKakaoSignInController(CustomerSignInService customerSignInService) {
        this.customerSignInService = customerSignInService;
    }

    //인가코드를 받기 위한 api 호출
    //반환되는 것 = 인가코드를 요청하는 url
    // 인가 코드를 발급 받기 위해, 어디까지 열람이 가능한 권한을 부여 할 것인지에 대한 동의서 화면에서 범위를 설정하고 확인 버튼을 누르면
    // 그에 맞는 코드를 발급하고 리디렉션주소와 함께 보냄
//    @GetMapping("/authorize")
//    public RedirectView authorize(@RequestParam(required = false) String scope) {
//        System.out.println("컨트롤러 RedirectView authorize 시작");
//        log.info("scope {} ", scope);
//
//        RedirectView redirectView = new RedirectView(customerSignInService.getAuthUrl(scope));
//
//        log.info("authorize redirectView {} ", redirectView);
//        System.out.println("컨트롤러 RedirectView authorize 끝");
//        return redirectView;
//    }
//
//    // 리다이렉트 페이지와 도착한 코드를 param으로 받음
//    @GetMapping("/redirect")
//    public ApiResponse<CustomerSignInResponse> handleRedirect(@RequestParam String code) {
//        System.out.println("컨트롤러 RedirectView handleRedirect 시작");
//        log.info("code {} ", code);
//        ApiResponse<CustomerSignInResponse> customerSignInProcess
//                = customerSignInService.kakaoCustomerSignInProcess(code);
//        return customerSignInProcess;
//    }
    //        boolean isSuccess = customerSignInService.handleAuthorizationCallback(code);
//        RedirectView redirectView = new RedirectView("/index.html?login=" + (b ? "success" : "error"));
//        log.info("handleRedirect redirectView {} ", redirectView);
//        System.out.println("컨트롤러 RedirectView handleRedirect 끝");
//        return redirectView;


//    @GetMapping("/profile")
//    public ResponseEntity<?> getProfile() {
//        return customerSignInService.getUserProfile();
//    }
//
//    @GetMapping("/friends")
//    public ResponseEntity<?> getFriends() {
//        return customerSignInService.getFriends();
//    }
//
//    @GetMapping("/message")
//    public ResponseEntity<?> sendMessage() {
//        return customerSignInService.sendMessage(customerSignInService.createDefaultMessage());
//    }
//
//    @GetMapping("/friend-message")
//    public ResponseEntity<?> sendMessageToFriend(@RequestParam String uuid) {
//        return customerSignInService.sendMessageToFriend(uuid, customerSignInService.createDefaultMessage());
//    }
//
//    @GetMapping("/logout")
//    public ResponseEntity<?> logout() {
//        return customerSignInService.logout();
//    }
//
//    @GetMapping("/unlink")
//    public ResponseEntity<?> unlink() {
//        return customerSignInService.unlink();
//    }
}
