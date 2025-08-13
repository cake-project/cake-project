package com.cakemate.cake_platform.domain.proposalFormChat.dto;

import com.cakemate.cake_platform.domain.proposalFormChat.entity.ChatRoomEntity;
import lombok.Getter;

/**
 * 채팅방 생성 성공 및 조회 시 응답에 사용
 */
@Getter
public class ChatRoomResponseDto {

    private final String roomId;

    public ChatRoomResponseDto(String roomId) {
        this.roomId = roomId;
    }


    //정적 팩토리 메서드
    // → 생성자 대신 of()를 통해 직관적으로 인스턴스를 생성할 수 있음
    public static ChatRoomResponseDto of(String roomId) {
        return new ChatRoomResponseDto(roomId);
    }


    //ChatRoomEntity 를 ChatRoomResponseDto 로 변환하여
    // 클라이언트 응답에 사용
    public static ChatRoomResponseDto from(ChatRoomEntity entity) {
        return of(entity.getId());
    }
}