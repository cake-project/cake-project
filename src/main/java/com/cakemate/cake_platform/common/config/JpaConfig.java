package com.cakemate.cake_platform.common.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    /**
     * Jpa의 Auditing기능(@CreateDate, @LastModifiedDate) 활성화 config
     */


}

