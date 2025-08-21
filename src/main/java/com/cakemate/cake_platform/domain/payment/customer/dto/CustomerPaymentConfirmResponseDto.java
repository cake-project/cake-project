package com.cakemate.cake_platform.domain.payment.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime approvedAt;
}
