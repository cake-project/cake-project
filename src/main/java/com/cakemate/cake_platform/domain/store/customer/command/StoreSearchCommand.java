package com.cakemate.cake_platform.domain.store.customer.command;

import lombok.Getter;

@Getter
public class StoreSearchCommand {
    private final String address;

    public StoreSearchCommand(String address) {
        this.address = address;
    }

    public boolean hasAddress() {
        return this.address != null && !this.address.isBlank();
    }
}
