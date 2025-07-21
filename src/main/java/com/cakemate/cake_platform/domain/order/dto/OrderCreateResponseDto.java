package com.cakemate.cake_platform.domain.order.dto;

import java.time.LocalDateTime;

public class OrderCreateResponseDto {

    private Long orderId;
    private String orderStatus;
    private String customerName;
    private String storeName;
    private LocalDateTime createdAt;

    public OrderCreateResponseDto(Long orderId, String orderStatus, String customerName, String storeName, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.customerName = customerName;
        this.storeName = storeName;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getStoreName() {
        return storeName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
