package com.cakemate.cake_platform.domain.requestForm.customer.dto.request;


import com.cakemate.cake_platform.common.commonEnum.CakeSize;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CustomerRequestFormCreateRequestDto {


    @NotNull(message = "제목을 입력하세요.")
    private String title;

    @NotNull(message = "내용을 입력하세요.")
    private String content;

    @NotNull(message = "지역을 입력하세요.")
    private String region;

    @NotNull(message = "가격을 입력하세요.")
    @Positive(message = "희망 가격은 0보다 커야 합니다.")
    private Integer desiredPrice;

    private String image;

    @NotNull(message = "픽업일을 입력하세요.")
    @Future(message = "픽업 날짜는 미래 시점이어야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime pickupDate;

    @NotNull(message = "케이크 사이즈를 입력하세요.")
    private CakeSize cakeSize;

    @Positive(message = "수량은 1 이상이어야 합니다.")
    private int quantity;
}

