package com.cakemate.cake_platform.domain.proposalFormChat.controller;

import com.cakemate.cake_platform.domain.proposalFormChat.service.ChatService;
import com.cakemate.cake_platform.domain.proposalFormChat.dto.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 채팅방 생성·조회 REST 엔드포인트를 담당하는 컨트롤러
 * <기능>
 * 1. POST /chat   : 채팅방 생성
 * 2. GET  /chat   : 전체 채팅방 목록 조회 -> 아직 진행 안함
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    /**
     * 채팅방 생성
     *
     * @param name 클라이언트가 전달한 채팅방 이름
     * @return 생성된 ChatRoom 객체(방 ID‧이름 포함)
     */
    // HTTP POST /chat
    @PostMapping
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}
