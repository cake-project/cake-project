package com.cakemate.cake_platform.domain.store.customer.command;

import lombok.Getter;

@Getter
public class SearchCommand {
    private final String address;

    public SearchCommand(String address) {
        this.address = address;
    }

    public boolean hasAddress() {
        return this.address != null && !this.address.isBlank();
    }
}
