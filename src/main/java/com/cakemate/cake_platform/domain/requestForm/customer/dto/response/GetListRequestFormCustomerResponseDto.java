package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;

import java.time.LocalDateTime;

public class GetListRequestFormCustomerResponseDto {
    private Long id;
    private String title;
    private RequestFormStatus requestStatus;
    private LocalDateTime createdAt;

    public GetListRequestFormCustomerResponseDto(
            Long id, String title, RequestFormStatus requestStatus, LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
    }
}
