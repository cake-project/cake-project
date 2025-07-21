package com.cakemate.cake_platform.domain.order.dto;

import com.cakemate.cake_platform.domain.requestForm.owner.dto.PageDto;

import java.util.List;

public class OrderPageResponseDto<T> {

    private List<T> orders;
    private PageDto pageDto;

    public OrderPageResponseDto(List<T> orders, PageDto pageDto) {
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
