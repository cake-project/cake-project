package com.cakemate.cake_platform.domain.proposalFormChat.entity;

import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.UUID;

@Entity
@Table(name = "chat_rooms")
@Getter
public class ChatRoomEntity {

    //채팅방 고유 ID(UUID 문자열)
    @Id
    private String id;

    // 견적서와 1:1 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_form_id")
    private ProposalForm proposalForm;

    // JPA 기본 생성자
    protected ChatRoomEntity() {}

    private ChatRoomEntity(String id, ProposalForm proposalForm) {
        this.id = id;
        this.proposalForm = proposalForm;
    }

    //견적서 기반 채팅방 생성 팩토리 메서드
    public static ChatRoomEntity create(ProposalForm proposalForm) {
        return new ChatRoomEntity(UUID.randomUUID().toString(), proposalForm);
    }
}
