package com.cakemate.cake_platform.domain.proposalForm.dto;

import java.time.LocalDateTime;

public class CommentDataDto {
    //속성
    private Long id;
    private Long customerId;
    private Long ownerId;
    private String content;
    private LocalDateTime createdAt;

    //생성자

    public CommentDataDto(Long id, Long customerId, Long ownerId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.content = content;
        this.createdAt = createdAt;
    }

    //게터
    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
