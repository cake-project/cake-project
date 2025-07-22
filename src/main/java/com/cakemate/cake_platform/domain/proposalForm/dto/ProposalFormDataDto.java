package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * proposalFormCreateResponseDto에서 활용하는 객체입니다!!
 */
public class ProposalFormDataDto {
    //속성
    private Long id;
    private String title;
    private String content;
    private int price;
    private LocalDateTime pickupDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;  // 날짜는 문자열로 포맷팅된 상태로 받는 게 편해요
    private String status;

    //생성자
    public ProposalFormDataDto(Long id, String title, String content, int price, LocalDateTime pickupDate, LocalDateTime createdAt, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.pickupDate = pickupDate;
        this.createdAt = createdAt;
        this.status = status;
    }

    //게터
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public int getPrice() { return price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
}

