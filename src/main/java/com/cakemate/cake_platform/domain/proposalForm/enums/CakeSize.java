package com.cakemate.cake_platform.domain.proposalForm.enums;

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

    public static CakeSize fromString(String str) {
        for (CakeSize cs : values()) {
            if (cs.size.equalsIgnoreCase(str)) {
                return cs;
            }
        }
        throw new IllegalArgumentException("일치하는 CakeSize가 없습니다: " + str);
    }
}
