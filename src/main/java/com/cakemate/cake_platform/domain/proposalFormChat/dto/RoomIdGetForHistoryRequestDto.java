package com.cakemate.cake_platform.domain.proposalFormChat.dto;

import lombok.Getter;

/**
 * 채팅 기록 내용 조회 요청 시 사용
 * ->클라이언트가 채팅방 메시지 내역을 요청할 때,
 * 어떤 방(roomId)의 내역을 조회할지 전달하는 DTO
 */
@Getter
public class RoomIdGetForHistoryRequestDto {
    //조회하려는 채팅방의 고유 ID
    private String roomId;
}
