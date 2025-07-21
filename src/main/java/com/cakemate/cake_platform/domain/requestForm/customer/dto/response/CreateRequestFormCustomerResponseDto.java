package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateRequestFormCustomerResponseDto {
    private Long id;
    private String title;
    private RequestFormStatus requestStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    public CreateRequestFormCustomerResponseDto(RequestForm requestForm) {
        this.id = requestForm.getId();
        this.title = requestForm.getTitle();
        this.requestStatus = requestForm.getStatus();
        this.createdAt = requestForm.getCreatedAt().toLocalDate();
    }
}
