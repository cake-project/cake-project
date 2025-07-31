package com.cakemate.cake_platform.common.command;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.exception.InvalidPasswordFormatException;
import com.cakemate.cake_platform.domain.auth.exception.PasswordMismatchException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class SearchCommand {
    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private String phoneNumber;
    private Long customerId;
    private Long ownerId;

    public SearchCommand(String email, String password, String passwordConfirm, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public SearchCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public SearchCommand(Long customerId, Long ownerId) {
        this.customerId = customerId;
        this.ownerId = ownerId;
    }

    public boolean hasEmail() {
        if (this.email != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasPassword() {
        if (this.password != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasPasswordConfirm() {
        if (this.passwordConfirm != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasName() {
        if (this.name != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasPhoneNumber() {
        if (this.phoneNumber != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMatchedPassword() {
        if (this.password.equals(this.passwordConfirm)) {
            return true;
        } else {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean hasPasswordPattern() {
        boolean matches
                = this.password
                .matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#.~_-])[A-Za-z\\d@$!%*?&#.~_-]{8,}$");
        if (matches) {
            return true;
        } else {
            throw new InvalidPasswordFormatException("비밀번호는 최소8자 이상, 하나 이상의 영문 대,소문자와 숫자+특수문자 조합이어야 합니다");
        }

    }

    public boolean hasCustomerId () {
        if (this.customerId != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasOwerId() {
        if (this.ownerId != null) {
            return true;
        } else {
            return false;
        }
    }
}
