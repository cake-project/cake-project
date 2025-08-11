package com.cakemate.cake_platform.domain.store.ranking.dto;

import lombok.Getter;

@Getter
public class StoreRankingResponseDto {
    private Long storeRank;
    private Long storeId;
    private String storeName;
    private Long orderCount;

    //기본 생성자
    public StoreRankingResponseDto() {}

    public StoreRankingResponseDto(Long storeRank, Long storeId, String storeName, Long orderCount) {
        this.storeRank = storeRank;
        this.storeId = storeId;
        this.storeName = storeName;
        this.orderCount = orderCount;
    }
}
