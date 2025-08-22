package com.cakemate.cake_platform.domain.proposalFormChat;

import com.cakemate.cake_platform.domain.proposalFormChat.dto.ChatMessageRequestDto;
import com.cakemate.cake_platform.domain.proposalFormChat.dto.ChatMessageResponseDto;
import com.cakemate.cake_platform.domain.proposalFormChat.enums.MessageType;
import com.cakemate.cake_platform.domain.proposalFormChat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {
    // JSON → DTO 변환
    private final ObjectMapper objectMapper;
    // 채팅 로직을 위임받는 서비스 클래스
    private final ChatService chatService;

    /**
     * 클라이언트가 WebSocket 연결을 맺으면 실행됨 (방 정보는 아직 모름)
     * → 이후 클라이언트가 ENTER 메시지를 보내면 방 정보를 알 수 있음
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 인터셉터에서 이미 검증/세팅됨
        String roomId = (String) session.getAttributes().get("roomId");
        Long memberId = (Long) session.getAttributes().get("memberId");
        String memberName = (String) session.getAttributes().getOrDefault("memberName", "UNKNOWN");


        if (roomId == null || roomId.isBlank() || memberId == null) {
            safeClose(session, CloseStatus.BAD_DATA.withReason("missing attributes"));
            return;
        }

        // 세션 등록 (1회)
        chatService.registerSession(roomId, session);

        // 입장 시스템 메시지 브로드캐스트
        ChatMessageResponseDto enter = ChatMessageResponseDto.builder()
                .type(MessageType.ENTER)
                .roomId(roomId)
                .sender(memberName)
                .message(memberName + "님이 입장했습니다.")
                .build();
        chatService.saveAndBroadcast(enter);
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
        if (expirationTime == null || expirationTime < System.currentTimeMillis()) {
            clearSessionAttributes(session);
            safeClose(session, new CloseStatus(4401, "token expired"));
            return;
        }

        // 2. 세션에서 사용자 이름 꺼냄 (없으면 "UNKNOWN"으로 대체)
        String memberName = (String) session.getAttributes().get("memberName");
        if (memberName == null) memberName = "UNKNOWN";

        String sessionRoomId = (String) session.getAttributes().get("roomId");
        Long memberId = (Long) session.getAttributes().get("memberId");

        if (sessionRoomId == null || memberId == null) {
            safeClose(session, CloseStatus.BAD_DATA.withReason("missing attributes"));
            return;
        }

        // JSON 문자열을 ChatMessage DTO 로 변환
        ChatMessageRequestDto dto = objectMapper.readValue(message.getPayload(), ChatMessageRequestDto.class);

        // TALK 만 허용 (ENTER 는 자동 처리이므로 무시)
        if (dto.getType() != MessageType.TALK) {
            return;
        }

        if (dto.getMessage() == null || dto.getMessage().isBlank()) {
            safeClose(session, CloseStatus.BAD_DATA.withReason("내용을 입력하세요."));
            return;
        }

        ChatMessageResponseDto messageResponseDto = ChatMessageResponseDto.builder()
                .type(MessageType.TALK)
                .roomId(sessionRoomId)
                .sender(memberName)
                .message(dto.getMessage())
                .build();

        //시스템 메시지 전송
        chatService.saveAndBroadcast(messageResponseDto);
    }
    /** 연결 종료 시 방에서 세션 해제 + 속성 정리 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomId = (String) session.getAttributes().get("roomId");
        if (roomId != null && !roomId.isBlank()) {
            chatService.unregisterSession(roomId, session);
        } else {
            log.warn("afterConnectionClosed: roomId missing. uri={}", session.getUri());
        }
        clearSessionAttributes(session);
    }

    private void clearSessionAttributes(WebSocketSession session) {
        Map<String, Object> attrs = session.getAttributes();
        attrs.remove("roomId");
        attrs.remove("memberId");
        attrs.remove("memberName");
        attrs.remove("memberType");
        attrs.remove("expirationTime");
    }

    private void safeClose(WebSocketSession session, CloseStatus status) {
        try { session.close(status); } catch (Exception ignore) {}
    }
}
