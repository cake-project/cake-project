package com.cakemate.cake_platform.domain.proposalFormChat;

import com.cakemate.cake_platform.common.exception.UnauthorizedAccessException;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.proposalFormChat.dto.ChatMessageRequestDto;
import com.cakemate.cake_platform.domain.proposalFormChat.dto.ChatMessageResponseDto;
import com.cakemate.cake_platform.domain.proposalFormChat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {
    // JSON → DTO 변환
    private final ObjectMapper objectMapper;
    // 채팅 로직을 위임받는 서비스 클래스
    private final ChatService chatService;


    /**
     * 아직 채팅방 입장(ENTER 메시지)을 하지 않은 세션들
     * → 연결만 하고 방 정보를 전달받지 않은 상태의 세션들을 임시 보관
     */
    private final Set<WebSocketSession> tempSessions = ConcurrentHashMap.newKeySet();

    /**
     * 클라이언트가 WebSocket 연결을 맺으면 실행됨 (방 정보는 아직 모름)
     * → 이후 클라이언트가 ENTER 메시지를 보내면 방 정보를 알 수 있음
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        tempSessions.add(session);
    }

    /**
     * 클라이언트가 메시지를 보내면 실행됨
     * → ENTER (입장) 메시지인지, TALK (채팅) 메시지인지 구분하여 처리
     */
    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message
    ) throws Exception {

        // 세션에서 토큰 만료 시간을 꺼냄
        Long expirationTime = (Long) session.getAttributes().get("expirationTime");

        // 만료된 토큰 트레픽 지우기 및 세션 종료
        // 1. 토큰이 없거나(expirationTime == null) 만료 시간 < 현재 시간 이면
        // -> 세션에 저장된 사용자 정보(memberId, expirationTime, memberName) 제거
        // -> "token expired(토큰 만료)" 사유로 WebSocket 세션 종료
        // (만료된 토큰인데도 세션에 memberId, memberName 같은 식별 정보가 그대로 남아 있으면,
        //이후에 서버 로직이 잘못 참조해서 만료된 사용자를 유효한 사용자처럼 처리할 위험이 있습니다.)
        if (expirationTime == null || expirationTime < System.currentTimeMillis() ) {
            session.getAttributes().remove("memberId");
            session.getAttributes().remove("expirationTime");
            session.getAttributes().remove("memberName");
            session.close(new CloseStatus(4401, "token expired"));
            return;
        }

        // 2. 세션에서 사용자 이름 꺼냄 (없으면 "UNKNOWN"으로 대체)
        String memberName = (String) session.getAttributes().get("memberName");
        if (memberName == null) memberName = "UNKNOWN";

        Long memberId = (Long) session.getAttributes().get("memberId");

        // JSON 문자열을 ChatMessage DTO 로 변환
        ChatMessageRequestDto dto = objectMapper.readValue(message.getPayload(), ChatMessageRequestDto.class);
        String roomId = dto.getRoomId();
        if (roomId == null || roomId.isBlank()) {
            session.close(CloseStatus.BAD_DATA.withReason("missing roomId"));
            return;
        }

        if (!chatService.canAccessRoom(memberId, roomId)) {
            session.close(new CloseStatus(4403, "유효하지 않은 사용자 유형입니다."));
            return;
        }

        //ENTER 메시지 처리
        if (dto.getType() == ChatMessageRequestDto.MessageType.ENTER) {

            // 세션을 실제 채팅방에 등록
            chatService.registerSession(roomId, session);

            //roomId 저장
            session.getAttributes().put("roomId", roomId);

            // 임시 세션 목록에서 제거
            tempSessions.remove(session);

            // 시스템 메시지 생성
            ChatMessageResponseDto messageResponseDto = ChatMessageResponseDto.builder()
                    .type(ChatMessageRequestDto.MessageType.ENTER)
                    .roomId(dto.getRoomId())
                    .sender(memberName)
                    .message(memberName + "님이 입장했습니다.")
                    .build();

            //시스템 메시지 전송
            chatService.saveAndBroadcast(messageResponseDto);
            return;
        }

        // TALK: 이미 위에서 권한 검증했으므로 바로 처리
        // ENTER 누락 대비
        chatService.registerSession(roomId, session);
        // 종료 정리용
        session.getAttributes().put("roomId", roomId);

        // TALK 메시지 처리
        ChatMessageResponseDto out = ChatMessageResponseDto.builder()
                .type(ChatMessageRequestDto.MessageType.TALK)
                .roomId(dto.getRoomId())
                .sender(memberName)
                .message(dto.getMessage())
                .build();
        //TALK 메시지 저장
        chatService.saveAndBroadcast(out);
    }

    /**
     *  클라이언트(WebSocket)가 연결을 종료했을 때 실행되는 메서드
     * → 세션을 채팅방에서 제거(메모리 낭비 방지)
     * 1. 종료된 세션의 roomId를 쿼리 파라미터에서 추출
     * 2. 해당 roomId의 채팅방에서 세션을 제거하여 메모리 낭비를 방지
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomId = (String) session.getAttributes().get("roomId");
        if (roomId != null && !roomId.isBlank()) {
            chatService.unregisterSession(roomId, session);
        } else {
            log.warn("afterConnectionClosed: roomId missing. uri={}", session.getUri());
        }
    }
}