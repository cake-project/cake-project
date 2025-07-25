package com.cakemate.cake_platform.domain.order.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomerOrderSummaryResponseDto {

    private String orderNumber;
    private String orderStatus;
    private String storeName;
    private LocalDateTime agreedPickupDate;
}
