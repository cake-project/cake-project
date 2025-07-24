package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerRequestFormCreateResponseDto {
    private Long id;
    private String title;
    private RequestFormStatus requestStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime pickupDate;

    public CustomerRequestFormCreateResponseDto(
            Long id, String title, RequestFormStatus requestStatus,
            LocalDateTime pickupDate, LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.requestStatus = requestStatus;
        this.pickupDate = pickupDate;
        this.createdAt = createdAt;
    }
}
