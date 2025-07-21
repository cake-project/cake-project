package com.cakemate.cake_platform.domain.requestForm.customer.dto.request;


import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateRequestFormCustomerRequestDto {
//    @NotNull(message = "고객 정보는 필수입니다.")

    private Customer customer;

    private ProposalForm proposalForm;
    @NotNull(message = "재목을 입력하세요.")
    private String title;
    @NotNull(message = "내용을 입력하세요.")
    private String content;
    @NotNull(message = "지역을 입력하세요.")
    private String region;
    @NotNull(message = "가격을 입력하세요.")
    private int desiredPrice;
    private String image;
    @NotNull(message = "픽업일을 입력하세요.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime pickupDate;

}

