package com.cakemate.cake_platform.domain.proposalFormChat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSockConfig implements WebSocketConfigurer {
    // WebSocket 연결 요청이 왔을 때 처리할 핸들러
    //-> 구현체: WebSockChatHandler
    private final WebSocketHandler webSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    /**
     * WebSocket URL 경로 등록 메서드
     * 클라이언트가 "/ws/chat" 경로로 WebSocket 연결을 시도하면,
     * webSocketHandler 가 연결을 처리함
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
