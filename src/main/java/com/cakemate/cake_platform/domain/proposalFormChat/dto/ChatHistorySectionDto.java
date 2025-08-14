package com.cakemate.cake_platform.domain.proposalFormChat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatHistorySectionDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String chatRoomId;
    private final List<ChatMessageHistoryResponseDto> messages;
}
