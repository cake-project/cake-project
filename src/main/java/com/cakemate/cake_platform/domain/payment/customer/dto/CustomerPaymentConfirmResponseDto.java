package com.cakemate.cake_platform.domain.payment.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomerPaymentConfirmResponseDto {

    private String paymentKey;
    private int totalAmount;
    private String method;
    private String receiptUrl;
    private LocalDateTime approvedAt;
}
