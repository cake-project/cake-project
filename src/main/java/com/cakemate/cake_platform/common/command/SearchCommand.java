package com.cakemate.cake_platform.common.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public class SearchCommand {
    private final String email;
    private final String password;
    private final String passwordConfirm;
    private final String name;
    private final String phoneNumber;

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



}
