package com.cakemate.cake_platform.domain.requestForm.owner.dto;

import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RequestFormDetailOwnerResponseDto {

    private Long requestFormId;
    private String customerName;
    private String title;
    private String cakeSize;
    private int quantity;
    private String region;
    private String content;
    private int desiredPrice;
    private String image;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime pickupDate;
    private RequestFormStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

}
