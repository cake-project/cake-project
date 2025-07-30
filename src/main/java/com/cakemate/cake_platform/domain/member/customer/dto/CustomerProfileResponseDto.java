package com.cakemate.cake_platform.domain.member.customer.dto;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerProfileResponseDto {
    private String password;
    private String passwordConfirm;
    private String name;
    private String email;
    private String phoneNumber;

    public static CustomerProfileResponseDto from(Customer customer) {
       return new CustomerProfileResponseDto(
               customer.getEmail(), customer.getPassword(), customer.getPasswordConfirm(),
               customer.getName(), customer.getPhoneNumber()
        );
    }

    public CustomerProfileResponseDto(String email, String password, String passwordConfirm, String name, String phoneNumber) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;

    }
}
