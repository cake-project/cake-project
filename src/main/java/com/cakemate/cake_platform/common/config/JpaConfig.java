package com.cakemate.cake_platform.common.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    /**
     * Jpa의 Auditing기능(@CreateDate, @LastModifiedDate) 활성화 config
     */

    //auditing 기능은 Instant·OffsetDateTime 뿐 아니라 LocalDateTime 도 지원
    @Bean
    public DateTimeProvider utcDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now(ZoneOffset.UTC));
    }

}

