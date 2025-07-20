package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GetDetailRequestFormCustomerResponseDto {
    private Long id;
    private String title;
    private String region;
    private String content;
    private Integer desiredPrice;
    private String image;
    private LocalDateTime pickupDate;
    private RequestFormStatus requestStatus;
    private LocalDateTime createdAt;


    public GetDetailRequestFormCustomerResponseDto(
            Long id, String title, String region,
            String content, Integer desiredPrice,
            String image, LocalDateTime pickupDate,
            RequestFormStatus requestStatus,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.region = region;
        this.content = content;
        this.desiredPrice = desiredPrice;
        this.image = image;
        this.pickupDate = pickupDate;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt;
    }
}
