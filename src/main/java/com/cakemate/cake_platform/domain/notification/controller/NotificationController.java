package com.cakemate.cake_platform.domain.notification.controller;

import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {
    private final  NotificationService notificationService;
    private final JwtUtil jwtUtil;

    public NotificationController(NotificationService notificationService, JwtUtil jwtUtil) {
        this.notificationService = notificationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(value = "/customer/subscribe", produces = "text/event-stream")
    public SseEmitter subscribeCustomerAPI(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader(value = "Last-Event-Id", required = false, defaultValue = "") String lastEventId) {
        Long customerId = jwtUtil.extractCustomerId(bearerToken);
        return notificationService.subscribeCustomer(customerId, lastEventId);
    }

    @GetMapping(value = "/owner/subscribe", produces = "text/event-stream")
    public SseEmitter subscribeOwnerAPI(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader(value = "Last-Event-Id", required = false, defaultValue = "") String lastEventId) {
        Long ownerId = jwtUtil.extractOwnerId(bearerToken);
        return notificationService.subscribeOwner(ownerId, lastEventId);
    }

    @PostMapping("/test")
    public void sendTestAPI(@RequestParam Long memberId, @RequestParam String memberType) {
        notificationService.sendNotification(memberId, "테스트 알림입니다! memberType : " + memberType, memberType);
    }
}
