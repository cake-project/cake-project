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
}
