package com.cakemate.cake_platform.domain.member.customer.dto.reponse;

import lombok.Getter;

@Getter
public class CustomerProfileResponseDto {
    private Long customerId;
    private String customerName;
    private String email;
    private String phoneNumber;

    public CustomerProfileResponseDto(Long customerId, String customerName, String email, String phoneNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
