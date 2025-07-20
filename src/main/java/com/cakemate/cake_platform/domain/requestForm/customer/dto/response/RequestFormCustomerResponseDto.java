package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RequestFormCustomerResponseDto {
    private Long id;
    private String title;
    private RequestFormStatus requestStatus;
    private LocalDate createdAt;

    public RequestFormCustomerResponseDto(RequestForm requestForm) {
        this.id = requestForm.getId();
        this.title = requestForm.getTitle();
        this.requestStatus = requestForm.getStatus();
        this.createdAt = requestForm.getCreatedAt().toLocalDate();
    }
}
