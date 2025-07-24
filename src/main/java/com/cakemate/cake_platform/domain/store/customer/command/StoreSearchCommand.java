package com.cakemate.cake_platform.domain.store.customer.command;

import com.cakemate.cake_platform.domain.store.owner.exception.AccessDeniedException;
import lombok.Getter;

@Getter
public class StoreSearchCommand {
    private final String address;
    private final Long customerId;


    public StoreSearchCommand(Long customerId, String address) {
        this.customerId = customerId;
        this.address = address;
    }

    public boolean hasAddress() {
        return address != null && !address.isBlank();
    }

}
