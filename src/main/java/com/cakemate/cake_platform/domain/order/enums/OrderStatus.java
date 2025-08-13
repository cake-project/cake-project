package com.cakemate.cake_platform.domain.order.enums;

public enum OrderStatus {
    MAKE_WAITING("MAKE_WAITING", "제작 대기 중"),
    READY_FOR_PICKUP("READY_FOR_PICKUP", "상품 픽업 대기 중"),
    COMPLETED("COMPLETED", "주문 완료"),
    CANCELLED("CANCELLED", "주문 취소");

    private final String strValue;
    private final String description;

    OrderStatus(String strValue, String description) {
        this.strValue = strValue;
        this.description = description;
    }

    // Enum 대소문자 구분 없이 사용하기 위해 사용하는 메서드
    public static OrderStatus fromString(String str) {
        for (OrderStatus p : values()) {
            if (p.strValue.equalsIgnoreCase(str)) {
                return p;
            }
        }
        // 일치하는 enum 이 없을 때 예외발생
        throw new IllegalArgumentException("일치하는 OrderStatus의 Enum이 없습니다 " + str);
    }
}
