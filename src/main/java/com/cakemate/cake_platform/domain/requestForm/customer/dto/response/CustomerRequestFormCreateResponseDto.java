package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerRequestFormCreateResponseDto {
    private Long customerId;
    private String customerName;
    private Long requestFormId;
    private String title;
    private RequestFormStatus requestStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime pickupDate;

    public CustomerRequestFormCreateResponseDto(
            Long customerId, String customerName,
            Long requestFormId, String title, RequestFormStatus requestStatus,
            LocalDateTime pickupDate, LocalDateTime createdAt
    ) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.requestFormId = requestFormId;
        this.title = title;
        this.requestStatus = requestStatus;
        this.pickupDate = pickupDate;
        this.createdAt = createdAt;
    }
}
