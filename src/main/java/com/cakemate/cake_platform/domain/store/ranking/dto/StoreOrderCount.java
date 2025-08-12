package com.cakemate.cake_platform.domain.store.ranking.dto;

import lombok.Getter;

@Getter
public class StoreOrderCount {
    private Long storeId;
    private String storeName;
    private Long orderCount;

    //기본 생성자
    public StoreOrderCount() {}

    public StoreOrderCount(Long storeId, String storeName, Long orderCount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.orderCount = orderCount;
    }
}
