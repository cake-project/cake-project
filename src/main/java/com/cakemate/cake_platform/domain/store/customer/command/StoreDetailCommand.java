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

    public void validateAuthenticatedCustomer() {
        if (customerId == null) {
            throw new AccessDeniedException("로그인한 고객만 접근할 수 있습니다.");
        }
    }
}
