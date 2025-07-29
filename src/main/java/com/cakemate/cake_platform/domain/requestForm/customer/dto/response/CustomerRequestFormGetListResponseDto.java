package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerRequestFormGetListResponseDto {
    private Long requestFormId;
    private String title;
    private RequestFormStatus requestStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public CustomerRequestFormGetListResponseDto(
            Long requestFormId, String title, RequestFormStatus requestStatus, LocalDateTime createdAt
    ) {
        this.requestFormId = requestFormId;
        this.title = title;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
    }
}
