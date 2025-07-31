package com.cakemate.cake_platform.domain.order.owner.dto;

import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerOrderStatusUpdateRequestDto {
    @NotNull(message = "주문 상태는 필수 값입니다.")
    private String orderStatus;

    public OrderStatus getOrderStatusEnum() {
        return OrderStatus.fromString(orderStatus);
    }

}
