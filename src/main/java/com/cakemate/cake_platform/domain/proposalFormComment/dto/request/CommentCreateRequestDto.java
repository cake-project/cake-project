package com.cakemate.cake_platform.domain.proposalFormComment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
    private String content;

    /**
     * 아래는 JSON → DTO 매핑용 기본 생성자 입니다.
     */
    public CommentCreateRequestDto() {

    }

}