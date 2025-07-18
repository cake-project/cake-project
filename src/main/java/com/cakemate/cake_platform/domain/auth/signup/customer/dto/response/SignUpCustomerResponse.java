package com.cakemate.cake_platform.domain.auth.signup.customer.dto.response;

import com.cakemate.cake_platform.domain.auth.signup.customer.entity.Customer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignUpCustomerResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final LocalDateTime createAt;

    public SignUpCustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.name = customer.getName();
        this.createAt = customer.getCreatedAt();
    }
}
