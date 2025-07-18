package com.cakemate.cake_platform.domain.auth.signup.owner.dto.response;

import com.cakemate.cake_platform.domain.auth.signup.customer.entity.Customer;
import com.cakemate.cake_platform.domain.auth.signup.owner.entity.Owner;

import java.time.LocalDateTime;

public class OwnerSignUpResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final LocalDateTime createAt;

    public OwnerSignUpResponse(Owner owner) {
        this.id = owner.getId();
        this.email = owner.getEmail();
        this.name = owner.getName();
        this.createAt = owner.getCreatedAt();

    }
}
