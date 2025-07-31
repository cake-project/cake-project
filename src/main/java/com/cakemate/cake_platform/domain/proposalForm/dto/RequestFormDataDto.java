package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.cakemate.cake_platform.common.commonEnum.CakeSize;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class RequestFormDataDto {
    //속성
    private Long requestFormId;
    private String title;
    private CakeSize cakeSize;
    private int quantity;
    private String region;
    private String content;
    private int desiredPrice;
    private String requestFormStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime desiredPickupDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private String image;

    //생성자
    public RequestFormDataDto(Long requestFormId, String title, String region, CakeSize cakeSize, int quantity, String content, Integer desiredPrice,
                              String image, LocalDateTime desiredPickupDate, String requestFormStatus,
                              LocalDateTime createdAt) {
        this.requestFormId = requestFormId;
        this.title = title;
        this.region = region;
        this.cakeSize = cakeSize;
        this.quantity = quantity;
        this.content = content;
        this.desiredPrice = desiredPrice;
        this.image = image;
        this.desiredPickupDate = desiredPickupDate;
        this.requestFormStatus = requestFormStatus;
        this.createdAt = createdAt;
    }

    //기능
}
