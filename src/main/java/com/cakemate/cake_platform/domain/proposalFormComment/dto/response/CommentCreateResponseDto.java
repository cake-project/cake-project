package com.cakemate.cake_platform.domain.proposalFormComment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
public class CommentCreateResponseDto {
    private Long commentId;
    private Long proposalFormId;
    private Long customerId;
    private Long ownerId;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private OffsetDateTime createdAt;

    public CommentCreateResponseDto(
            Long commentId, Long proposalFormId, Long customerId,
            Long ownerId, String content, OffsetDateTime createdAt
    ) {
        this.commentId = commentId;
        this.proposalFormId = proposalFormId;
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
