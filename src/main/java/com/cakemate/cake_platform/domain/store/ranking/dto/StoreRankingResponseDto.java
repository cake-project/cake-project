package com.cakemate.cake_platform.domain.store.ranking.dto;

import lombok.Getter;

@Getter
public class StoreRankingResponseDto {
    private int rank;
    private Long storeId;
    private String storeName;
    private Long orderCount;

    //기본 생성자
    public StoreRankingResponseDto() {}

    public StoreRankingResponseDto(int rank, Long storeId, String storeName, Long orderCount) {
        this.rank = rank;
        this.storeId = storeId;
        this.storeName = storeName;
        this.orderCount = orderCount;
    }
}
