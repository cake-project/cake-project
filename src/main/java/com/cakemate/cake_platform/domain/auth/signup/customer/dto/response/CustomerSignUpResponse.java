package com.cakemate.cake_platform.domain.auth.signup.customer.dto.response;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerSignUpResponse {
    private final Long customerId;
    private final String email;
    private final String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    public CustomerSignUpResponse(Customer customer) {
        this.customerId = customer.getId();
        this.email = customer.getEmail();
        this.name = customer.getName();
        this.createdAt = customer.getCreatedAt();
    }
}
