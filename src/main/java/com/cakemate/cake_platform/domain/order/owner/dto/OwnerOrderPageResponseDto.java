package com.cakemate.cake_platform.domain.order.owner.dto;

import com.cakemate.cake_platform.common.dto.PageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OwnerOrderPageResponseDto<T> {

    private List<T> orders;
    private PageDto pageDto;
}
