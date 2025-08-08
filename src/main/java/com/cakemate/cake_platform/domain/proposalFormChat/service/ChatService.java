package com.cakemate.cake_platform.domain.proposalFormChat.service;

import com.cakemate.cake_platform.domain.auth.exception.BadRequestException;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalFormChat.dto.ChatRoom;
import com.cakemate.cake_platform.domain.proposalFormChat.entity.ChatRoomEntity;
import com.cakemate.cake_platform.domain.proposalFormChat.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    //객체 → JSON 직렬화에 사용하는 Jackson Mapper
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    /**
     * 견적서 기준으로 채팅방을 찾는 메서드
     * (소비자 ACCEPT 시 호출 )
     * */
    //견적서 기준으로 채팅방을 찾고,
    //없으면 (하나의 견적서에 하나의 방만 생기도록 보장 하기 위헤)새로 저장
    //->사용자가 브라우저를 새로 열어도 DB에 roomId 가 남아 있으니 같은 채팅방으로 다시 연결할 수 있음
    @Transactional
    public ChatRoomEntity createRoomIfAbsent(ProposalForm proposalForm) {
        return chatRoomRepository.findByProposalForm_Id(proposalForm.getId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoomEntity.create(proposalForm)));
    }

    /**
     * 점주 CONFIRM 시 호출
     * ➜ 반드시 존재해야 함. 없으면 비즈니스 예외 throw
     */
    @Transactional(readOnly = true)
    public ChatRoomEntity findRoomOrThrow(Long proposalFormId) {
        return chatRoomRepository
                .findByProposalForm_Id(proposalFormId)
                .orElseThrow(() ->
                        new BadRequestException("소비자가 선택한 채팅방이 아직 생성되지 않았습니다."));
    }

    @Transactional(readOnly = true)
    public Optional<String> findRoomId(Long proposalFormId) {
        return chatRoomRepository
                .findByProposalForm_Id(proposalFormId)
                .map(ChatRoomEntity::getId);
    }

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
