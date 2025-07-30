package com.cakemate.cake_platform.domain.member.customer.dto.reponse;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import lombok.Getter;

@Getter
public class UpdateCustomerProfileResponseDto {
    private Long customerId;
    private String email;
    private String customerName;
    private String phoneNumber;

    public static UpdateCustomerProfileResponseDto from(Customer customer) {
        return new UpdateCustomerProfileResponseDto(
                customer.getId(), customer.getEmail(),
                customer.getName(), customer.getPhoneNumber()
        );
    }

    public UpdateCustomerProfileResponseDto(Long customerId, String email, String customerName, String phoneNumber) {
        this.customerId = customerId;
        this.email = email;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
    }
}
