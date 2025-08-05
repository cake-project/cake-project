package com.cakemate.cake_platform.domain.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    READY("READY", "결제 생성"),
    IN_PROGRESS("IN_PROGRESS", "결제 정보 인증 완료"),
    WAITING_FOR_DEPOSIT("WAITING_FOR_DEPOSIT", "가상 계좌 입금 대기"),
    DONE("DONE", "결제 승인"),
    CANCELED("CANCELED", "결제 취소"),
    PARTIAL_CANCELED("PARTIAL_CANCELED", "결제 부분 취소"),
    ABORTED("ABORTED", "결제 승인 실패"),
    EXPIRED("EXPIRED", "결제 유효 시간 만료");

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
