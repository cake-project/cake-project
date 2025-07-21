package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CreateRequestFormCustomerResponseDto {
    private Long id;
    private String title;
    private RequestFormStatus requestStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public CreateRequestFormCustomerResponseDto(
            Long id, String title, RequestFormStatus requestStatus, LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
    }
}
