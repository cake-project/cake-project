package com.cakemate.cake_platform.domain.payment.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerPaymentConfirmRequestDto {

    private String paymentKey;
    private String orderId;
    private int amount;
}
