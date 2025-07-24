package com.cakemate.cake_platform.domain.store.customer.command;

import com.cakemate.cake_platform.domain.store.owner.exception.AccessDeniedException;
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
    // ownerId 검증 (인증된 점주인지 체크)
    public void validateAuthenticatedOwner() {
        if (ownerId == null) {
            throw new AccessDeniedException("로그인한 점주만 접근할 수 있습니다.");
        }
    }

    // storeId 존재 여부 체크
    public boolean hasStoreId() {
        return storeId != null;
    }
}
