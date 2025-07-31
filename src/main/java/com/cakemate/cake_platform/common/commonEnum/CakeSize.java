package com.cakemate.cake_platform.common.commonEnum;

import com.cakemate.cake_platform.common.exception.InvalidCakeSizeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum CakeSize {
    DOSIRAK("도시락", 10, 10000),
    MINI("미니", 12, 15000),
    SIZE_1("1호", 16, 20000),
    SIZE_2("2호", 18, 25000);

    private final String size;
    private final int diameterCm;
    private final int minPrice;

    CakeSize(String size, int diameterCm, int minPrice) {
        this.size = size;
        this.diameterCm = diameterCm;
        this.minPrice = minPrice;
    }

    @JsonCreator
    public static CakeSize fromString(String str) {
        for (CakeSize cs : values()) {
            // 영문 enum 이름 (DOSIRAK 등)과 일치하거나, 한글 표기("도시락" 등)과도 일치하면 반환
            if (cs.name().equalsIgnoreCase(str) || cs.size.equals(str)) {
                return cs;
            }
        }
        throw new InvalidCakeSizeException("일치하는 CakeSize가 없습니다: " + str);
    }
}
