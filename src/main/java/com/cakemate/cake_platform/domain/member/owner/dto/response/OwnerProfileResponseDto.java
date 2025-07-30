package com.cakemate.cake_platform.domain.member.owner.dto.response;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import lombok.Getter;

@Getter
public class OwnerProfileResponseDto {

    private String password;
    private String passwordConfirm;
    private String name;
    private String email;
    private String phoneNumber;

    public static OwnerProfileResponseDto from(Owner owner) {
       return new OwnerProfileResponseDto(
               owner.getEmail(), owner.getPassword(), owner.getPasswordConfirm(),
               owner.getName(), owner.getPhoneNumber()
        );
    }

    public OwnerProfileResponseDto(String email, String password, String passwordConfirm, String name, String phoneNumber) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
