package com.cakemate.cake_platform.domain.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    AWAITING_PAYMENT("AWAITING_PAYMENT", "결제 대기"),
    IN_PROGRESS("IN_PROGRESS", "결제 시작"),
    AUTHENTICATED("AUTHENTICATED", "결제 인증 성공"),
    APPROVING("APPROVING", "결제 승인 요청"),
    COMPLETED("COMPLETED", "결제 완료"),
    FAILED("FAILED", "결제 실패");

    private final String strValue;
    private final String description;

    PaymentStatus(String strValue, String description) {
        this.strValue = strValue;
        this.description = description;
    }

    public static PaymentStatus fromString(String str) {
        for (PaymentStatus p : values()) {
            if (p.strValue.equalsIgnoreCase(str)) {
                return p;
            }
        }

        throw new IllegalArgumentException("일치하는 PaymentStatus의 Enum이 없습니다." + str);
    }
}
