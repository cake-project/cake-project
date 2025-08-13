package com.cakemate.cake_platform.domain.proposalFormChat.repository;

import com.cakemate.cake_platform.domain.proposalFormChat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {

    //특정 견적서에 연결된 채팅방 조회 (없으면 빈 화면)
    Optional<ChatRoomEntity> findByProposalForm_Id(Long proposalFormId);
}
