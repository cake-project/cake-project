package com.cakemate.cake_platform.domain.auth.signup.customer.dto.request;

import com.cakemate.cake_platform.domain.auth.signup.customer.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerSignUpRequest {
    private final String email;
    private final String password;
    private final String passwordConfirm;
    private final String name;
    private final String phoneNumber;

    public CustomerSignUpRequest(Customer customer) {
        this.email = customer.getEmail();
        this.password = customer.getPassword();
        this.passwordConfirm = customer.getPasswordConfirm();
        this.name = customer.getName();
        this.phoneNumber = customer.getPhoneNumber();
    }

}
