package com.cakemate.cake_platform.domain.proposalForm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDataDto {
    //속성
    private Long commentId;
    private Long customerId;
    private Long ownerId;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    //생성자

    public CommentDataDto(Long commentId, Long customerId, Long ownerId, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.content = content;
        this.createdAt = createdAt;
    }

    //기능
}
