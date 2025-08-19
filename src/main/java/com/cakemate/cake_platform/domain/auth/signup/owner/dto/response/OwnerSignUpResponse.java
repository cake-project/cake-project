package com.cakemate.cake_platform.domain.auth.signup.owner.dto.response;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class OwnerSignUpResponse {
    private final Long ownerId;
    private final String email;
    private final String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    public OwnerSignUpResponse(Owner owner) {
        this.ownerId = owner.getId();
        this.email = owner.getEmail();
        this.name = owner.getName();
        this.createdAt = owner.getCreatedAt();
    }
}