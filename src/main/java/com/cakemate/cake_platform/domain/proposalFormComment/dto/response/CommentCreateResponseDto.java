package com.cakemate.cake_platform.domain.proposalFormComment.dto.response;
import lombok.Getter;

@Getter
public class CommentCreateResponseDto {
    private Long commentId;
    private Long proposalFormId;
    private Long customerId;
    private Long ownerId;
    private String content;

    private CommentCreateResponseDto(
            Long commentId, Long proposalFormId, Long customerId,
            Long ownerId, String content
    ) {
        this.commentId = commentId;
        this.proposalFormId = proposalFormId;
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.content = content;
    }
    public CommentCreateResponseDto() {}

    public CommentCreateResponseDto createOwnerCommentResponseDto(Long commentId, Long proposalFormId,
                                                                  Long ownerId, String content) {
        return new CommentCreateResponseDto(commentId, proposalFormId, null, ownerId, content);
    }
    public CommentCreateResponseDto createCustomerCommentResponseDto(Long commentId, Long proposalFormId,
                                                                     Long customerId, String content) {
        return new CommentCreateResponseDto(commentId, proposalFormId, customerId, null, content);
    }
}