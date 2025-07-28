package com.cakemate.cake_platform.domain.store.customer.command;

import com.cakemate.cake_platform.domain.store.owner.exception.AccessDeniedException;
import lombok.Getter;

@Getter
public class StoreDetailCommand {
    private final Long customerId;
    private final Long storeId;

    public StoreDetailCommand(Long customerId, Long storeId) {
        this.customerId = customerId;
        this.storeId = storeId;
    }

}
