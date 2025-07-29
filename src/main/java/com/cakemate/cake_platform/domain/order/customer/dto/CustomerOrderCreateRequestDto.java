package com.cakemate.cake_platform.domain.order.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerOrderCreateRequestDto {

    private String customerName;
    private Long requestFormId;
    private Long proposalFormId;
}
