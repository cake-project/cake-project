package com.cakemate.cake_platform.domain.proposalFormComment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentCreateResponseDto {
    private Long id;
    private Long proposalFormId;
    private Long customerId;
    private Long ownerId;
    private String content;

    public CommentCreateResponseDto(
            Long id, Long proposalFormId, Long customerId,
            Long ownerId, String content
    ) {
        this.id = id;
        this.proposalFormId = proposalFormId;
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.content = content;
    }
}
