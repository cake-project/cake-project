package com.cakemate.cake_platform.domain.order.customer.dto;

import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class CustomerOrderCreateResponseDto {

    private String orderId;
    private OrderStatus orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public CustomerOrderCreateResponseDto(String orderId, OrderStatus orderStatus, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }
}
