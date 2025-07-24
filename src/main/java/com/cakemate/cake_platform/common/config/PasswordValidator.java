package com.cakemate.cake_platform.common.config;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    /**
     * 아래는 (점주) 내 정보 수정 서비스에서 비밀번호 변경 시 사용하는 메소드 입니다.
     */
    //비밀번호가 null 이 아니고, 비밀번호가 빈 문자열(password.isBlank)이면 안된다(!).
    public boolean isPasswordChangeRequested(String password) {
        return password != null && !password.isBlank();
    }

    /**
     * 아래는 (점주) 내 정보 수정 서비스에서
     * 비밀번호와 비밀번호 확인값이 일치하는지 검증하는 메소드입니다.
     */
    //비밀번호 확인값이 null 이거나, 비밀번호와 일치하지 않으면 예외를 발생시킨다.
    public void validatePasswordMatch(String password, String passwordConfirm) {
        if (passwordConfirm == null || !password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
    }


}
