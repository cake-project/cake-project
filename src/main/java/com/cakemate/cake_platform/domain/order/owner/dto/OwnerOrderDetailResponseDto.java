package com.cakemate.cake_platform.domain.order.owner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OwnerOrderDetailResponseDto {

    private Long orderId;
    private String orderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime orderCreatedAt;
    private String orderStatus;
    private String customerName;
    private String customerPhoneNumber;
    private Long requestFormId;
    private Long proposalFormId;
    private String productName;
    private String cakeSize;
    private int quantity;
    private int agreedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime agreedPickupDate;
    private String finalCakeImage;
}