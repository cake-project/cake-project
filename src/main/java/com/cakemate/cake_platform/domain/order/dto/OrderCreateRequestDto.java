package com.cakemate.cake_platform.domain.order.dto;

public class OrderCreateRequestDto {

    private String customerName;

    public OrderCreateRequestDto(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }
}
