package com.cakemate.cake_platform.domain.order.dto;

import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class OrderCreateResponseDto {

    private String orderId;
    private OrderStatus orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public OrderCreateResponseDto(String orderId, OrderStatus orderStatus, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }
}
