package com.cakemate.cake_platform.domain.proposalFormChat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * WebSocket 실시간 채팅에서 응답
 * -> 클라이언트 ↔ 서버 간에 주고받는 단일 메시지를 표현.
 */
@Getter
@Setter
@Builder
public class ChatMessageResponseDto {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    // 메시지 타입(ENTER, TALK)
    private ChatMessageRequestDto.MessageType type;

    private String sender;

    // 채팅방 ID
    private String roomId;

    // 메시지 본문
    private String message;
}
