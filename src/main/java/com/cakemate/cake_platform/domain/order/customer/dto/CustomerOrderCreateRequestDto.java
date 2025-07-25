package com.cakemate.cake_platform.domain.order.customer.dto;

public class CustomerOrderCreateRequestDto {

    private String customerName;

    public CustomerOrderCreateRequestDto(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }
}
