package com.cakemate.cake_platform.domain.proposalFormChat.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * WebSocket 채팅에서 클라이언트 ↔ 서버 간에 주고받는 단일 메시지를 표현하는 DTO.
 */
@Getter
@Setter
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type; // 메시지 타입(ENTER, TALK)
    private String roomId; // 채팅방 ID
    private String sender; // 메시지 보낸 사람 식별자(이름 또는 아이디)
    private String message; // 메시지 본문
}