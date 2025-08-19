package com.cakemate.cake_platform.domain.member.owner.dto.response;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import lombok.Getter;

@Getter
public class OwnerProfileResponseDto {
    private String name;
    private String email;
    private String phoneNumber;

    public OwnerProfileResponseDto(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
