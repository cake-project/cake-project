package com.cakemate.cake_platform.domain.order.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomerOrderDetailResponseDto {

    private String orderId;
    private Long requestFormId;
    private Long proposalFormId;
    private String orderStatus;
    private String customerName;
    private String storeBusinessName;
    private String storeName;
    private String productName;
    private String storePhoneNumber;
    private String storeAddress;
    private int agreedPrice;
    private LocalDateTime agreedPickupDate;
    private String finalCakeImage;

}
