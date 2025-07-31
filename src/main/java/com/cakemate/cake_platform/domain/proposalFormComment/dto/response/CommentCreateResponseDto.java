package com.cakemate.cake_platform.domain.proposalFormComment.dto.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
public class CommentCreateResponseDto {
    private Long commentId;
    private Long proposalFormId;
    private Long customerId;
    private Long ownerId;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;  // 생성일 추가

    private CommentCreateResponseDto(
            Long commentId, Long proposalFormId, Long customerId,
            Long ownerId, String content, LocalDateTime createdAt
    ) {
        this.commentId = commentId;
        this.proposalFormId = proposalFormId;
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.content = content;
        this.createdAt = createdAt;
    }
    public CommentCreateResponseDto() {}


    public CommentCreateResponseDto createOwnerCommentResponseDto(
            Long commentId, Long proposalFormId, Long ownerId, String content, LocalDateTime createdAt
    ) {
        return new CommentCreateResponseDto(commentId, proposalFormId, null, ownerId, content, createdAt);
    }

    public CommentCreateResponseDto createCustomerCommentResponseDto(
            Long commentId, Long proposalFormId, Long customerId, String content, LocalDateTime createdAt
    ) {
        return new CommentCreateResponseDto(commentId, proposalFormId, customerId, null, content, createdAt);
    }
}