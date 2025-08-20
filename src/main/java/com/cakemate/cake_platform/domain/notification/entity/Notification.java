package com.cakemate.cake_platform.domain.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notifications")
public class Notification {
    //속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId;
    private String message;

    @Column(name = "is_read")
    private boolean read = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "receiver_type", nullable = false)
    private String memberType;
    //생성자
    public Notification() {}

    public Notification(Long receiverId, String message, String memberType) {
        this.receiverId = receiverId;
        this.message = message;
        this.memberType = memberType;
    }

    //알림 읽음 처리 기능
    public void markAsRead() {
        this.read = true;
        this.readAt = LocalDateTime.now();
    }
}
