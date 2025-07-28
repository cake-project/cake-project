package com.cakemate.cake_platform.domain.proposalForm.dto;

import java.time.LocalDateTime;

public class CommentDataDto {
    //속성
    private Long commentId;
    private Long customerId;
    private Long ownerId;
    private String content;
    private LocalDateTime createdAt;

    //생성자

    public CommentDataDto(Long commentId, Long customerId, Long ownerId, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.content = content;
        this.createdAt = createdAt;
    }

    //게터
    public Long getCommentId() {
        return commentId;
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
