package com.cakemate.cake_platform.domain.requestForm.owner.dto;

import com.cakemate.cake_platform.common.dto.PageDto;

import java.util.List;

public class RequestFormPageOwnerResponseDto<T> {

    private List<T> requestForms;
    private PageDto pageDto;

    public RequestFormPageOwnerResponseDto(List<T> requestForms, PageDto pageDto) {
        this.requestForms = requestForms;
        this.pageDto = pageDto;
    }

    public List<T> getRequestForms() {
        return requestForms;
    }

    public PageDto getPageDto() {
        return pageDto;
    }
}
