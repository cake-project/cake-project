package com.cakemate.cake_platform.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationDto {
    //속성
    private String eventId;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    //생성자
    public NotificationDto(String eventId, String message, LocalDateTime createdAt) {
        this.eventId = eventId;
        this.message = message;
        this.createdAt = createdAt;
    }

    //기능
}
