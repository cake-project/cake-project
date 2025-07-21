package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetListRequestFormCustomerResponseDto {
    private Long RequestFormId;
    private String title;
    private RequestFormStatus requestStatus;
    private LocalDateTime createdAt;

    public GetListRequestFormCustomerResponseDto(
            Long RequestFormId, String title, RequestFormStatus requestStatus, LocalDateTime createdAt
    ) {
        this.RequestFormId = RequestFormId;
        this.title = title;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
    }
}
