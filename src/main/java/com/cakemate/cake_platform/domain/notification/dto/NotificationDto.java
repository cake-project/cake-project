package com.cakemate.cake_platform.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationDto {
    //속성
    private String eventId;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    //생성자
    public NotificationDto(String eventId, String content, LocalDateTime createdAt) {
        this.eventId = eventId;
        this.content = content;
        this.createdAt = createdAt;
    }

    //기능
}
