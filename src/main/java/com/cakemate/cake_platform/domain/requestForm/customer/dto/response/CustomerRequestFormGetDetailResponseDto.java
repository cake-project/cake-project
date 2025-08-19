package com.cakemate.cake_platform.domain.requestForm.customer.dto.response;

import com.cakemate.cake_platform.common.commonEnum.CakeSize;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jdk.jshell.Snippet;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CustomerRequestFormGetDetailResponseDto {
    //속성
    private RequestFormGetDetailDto requestForm;
    private List<ProposalGetListInternalDto> proposalsList;

    //생성자
    public CustomerRequestFormGetDetailResponseDto(
            RequestFormGetDetailDto requestForm, List<ProposalGetListInternalDto> proposalsList
    ) {
        this.requestForm = requestForm;
        this.proposalsList = proposalsList;
    }
    /**
     * 아래는 RequestForm 단건 조회를 위한 내부 클래스 입니다.
     */
    @Getter
    public static class RequestFormGetDetailDto {
        //속성
        private Long requestFormId;
        private String title;
        private String region;
        private String content;
        private Integer desiredPrice;
        private String image;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime pickupDate;
        private RequestFormStatus requestStatus;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;

        private CakeSize cakeSize;
        private int quantity;

        //생성자
        public RequestFormGetDetailDto(
                Long requestFormId, String title,
                String region, CakeSize cakeSize, int quantity,
                String content, Integer desiredPrice,
                String image, LocalDateTime pickupDate,
                RequestFormStatus requestStatus,
                LocalDateTime createdAt
        ) {
            this.requestFormId = requestFormId;
            this.title = title;
            this.region = region;
            this.cakeSize = cakeSize;
            this.quantity = quantity;
            this.content = content;
            this.desiredPrice = desiredPrice;
            this.image = image;
            this.pickupDate = pickupDate;
            this.requestStatus = requestStatus;
            this.createdAt = createdAt;
        }

        //기능

    }


    /**
     * 아래는 private List<> proposalsList 를 위한 내부 클래스 입니다.
     */
    @Getter
    public static class ProposalGetListInternalDto {
        //속성
        @Column(nullable = false)
        private Long proposalFormId;

        @Column(nullable = false)
        private String storeName;

        @Column(nullable = false)
        private String title;

        @Column(nullable = false)
        private String content;

        @Column(nullable = false)
        private Integer proposedPrice;

        @Column(nullable = false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime proposedPickupDate;

        private String image;

        @Column(nullable = false)
        @Enumerated(value = EnumType.STRING)
        private ProposalFormStatus status;

        @CreatedDate
        @Column(nullable = false, updatable = false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;

        private CakeSize cakeSize;
        private Integer quantity;


        //생성자

        public ProposalGetListInternalDto(
                Long proposalFormId, String storeName, String title,
                CakeSize cakeSize, Integer quantity,
                String content, Integer proposedPrice,
                LocalDateTime proposedPickupDate, String image,
                ProposalFormStatus status, LocalDateTime createdAt
        ) {
            this.proposalFormId = proposalFormId;
            this.storeName = storeName;
            this.title = title;
            this.cakeSize = cakeSize;
            this.quantity = quantity;
            this.content = content;
            this.proposedPrice = proposedPrice;
            this.proposedPickupDate = proposedPickupDate;
            this.image = image;
            this.status = status;
            this.createdAt = createdAt;
        }
    }


}
