package com.cakemate.cake_platform.domain.proposalFormChat.repository;

import com.cakemate.cake_platform.domain.proposalFormChat.entity.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 채팅 메시지 엔티티용 JPA 리포지토리
 * → 기본 CRUD 외에도 roomId로 메시지를 정렬/페이징 조회 가능
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    //특정 채팅방의 메시지를 id 기준 오름차순 정렬
    //→ 채팅 내역을 시간순으로 화면에 띄울 때 사용
    List<ChatMessageEntity> findByRoomIdOrderByCreatedAtAsc(String roomId);
}
