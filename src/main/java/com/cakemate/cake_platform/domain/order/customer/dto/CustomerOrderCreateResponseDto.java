package com.cakemate.cake_platform.domain.order.customer.dto;

import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class CustomerOrderCreateResponseDto {

    private Long orderId;
    private String orderNumber;
    private OrderStatus orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime orderCreatedAt;
    private String currency = "KRW";
    private int amount;
    private String orderName;
    private String customerEmail;
    private String customerName;
    private String customerMobilePhone;

    public CustomerOrderCreateResponseDto(Long orderId, String orderNumber, OrderStatus orderStatus, LocalDateTime orderCreatedAt, int amount, String orderName, String customerEmail, String customerName, String customerMobilePhone) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderCreatedAt = orderCreatedAt;
        this.amount = amount;
        this.orderName = orderName;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.customerMobilePhone = customerMobilePhone;
    }
}
