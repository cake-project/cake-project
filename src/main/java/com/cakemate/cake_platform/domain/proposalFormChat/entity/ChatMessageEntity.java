package com.cakemate.cake_platform.domain.proposalFormChat.entity;

import com.cakemate.cake_platform.domain.proposalFormChat.dto.ChatMessageResponseDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 실제 대화 한 줄(메시지)을 DB에 저장하는 엔티티
 */
@Builder
@Getter
@Entity
@Table(name = "chat_messages",
        indexes = @Index(name = "idx_room_created", columnList = "roomId, createdAt"))
// createdAt 자동 세팅을 위한 리스너
@EntityListeners(AuditingEntityListener.class)
// builder 패턴용 private 생성자 자동 생성
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String roomId;


    @Column(nullable = false, length = 64)
    private String senderName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    // JPA 기본 생성자
    protected ChatMessageEntity() {}

    //ChatMessage DTO → ChatMessageEntity 변환
    //→ WebSocket 으로 받은 메시지를 DB에 저장할 수 있도록 변환
    public static ChatMessageEntity from(ChatMessageResponseDto dto) {
        return ChatMessageEntity.builder()
                .roomId(dto.getRoomId())
                .senderName(dto.getSender())
                .content(dto.getMessage())
                .build();
    }
}
