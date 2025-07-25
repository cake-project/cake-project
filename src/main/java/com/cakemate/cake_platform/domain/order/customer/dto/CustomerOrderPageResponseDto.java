package com.cakemate.cake_platform.domain.order.customer.dto;

import com.cakemate.cake_platform.common.dto.PageDto;

import java.util.List;

public class CustomerOrderPageResponseDto<T> {

    private List<T> orders;
    private PageDto pageDto;

    public CustomerOrderPageResponseDto(List<T> orders, PageDto pageDto) {
        this.orders = orders;
        this.pageDto = pageDto;
    }

    public List<T> getOrders() {
        return orders;
    }

    public PageDto getPageDto() {
        return pageDto;
    }

}
