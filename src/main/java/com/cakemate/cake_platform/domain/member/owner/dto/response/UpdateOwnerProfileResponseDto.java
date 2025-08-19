package com.cakemate.cake_platform.domain.member.owner.dto.response;

import lombok.Getter;

@Getter
public class UpdateOwnerProfileResponseDto {
    private Long ownerId;
    private String email;
    private String name;
    private String phoneNumber;

    public UpdateOwnerProfileResponseDto(Long ownerId, String name, String email, String phoneNumber) {
        this.ownerId = ownerId;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
