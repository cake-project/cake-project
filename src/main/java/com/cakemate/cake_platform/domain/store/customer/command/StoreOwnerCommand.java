package com.cakemate.cake_platform.domain.store.customer.command;

import lombok.Getter;

@Getter
public class StoreOwnerCommand {
    private Long ownerId;
    private Long storeId;

    public StoreOwnerCommand(Long ownerId, Long storeId) {
        this.ownerId = ownerId;
        this.storeId = storeId;
    }

    public StoreOwnerCommand(Long ownerId) {
        this.ownerId = ownerId;
    }


    // storeId 존재 여부 체크
    public boolean hasStoreId() {
        return storeId != null;
    }
}
