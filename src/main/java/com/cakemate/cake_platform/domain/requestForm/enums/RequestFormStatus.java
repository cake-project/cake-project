package com.cakemate.cake_platform.domain.requestForm.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RequestFormStatus {
    REQUESTED("REQUESTED","의뢰 대기 중"),
    ESTIMATING("ESTIMATING","의뢰 컨택 중"),
    SELECTED("SELECTED","의뢰 수주 완료")
    ;
    private final String strValue;
    private final String description;

    RequestFormStatus(String strValue, String description) {
        this.strValue = strValue;
        this.description = description;
    }

    // Enum 대소문자 구분 없이 사용하기 위해 사용하는 메서드
    public static RequestFormStatus fromString(String str) {
        for (RequestFormStatus p : values()) {
            if (p.strValue.equalsIgnoreCase(str)) {
                return p;
            }
        }
        // 일치하는 enum 이 없을 때 예외발생
        throw new IllegalArgumentException("일치하는 RequestFormStatus의 Enum이 없습니다 " + str);
    }
}
