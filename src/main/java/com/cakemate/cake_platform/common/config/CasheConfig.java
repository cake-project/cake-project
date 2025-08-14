package com.cakemate.cake_platform.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CasheConfig {
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)// 쓰기 시점 기준으로 TTL적용 60분
                .maximumSize(200);// 최대 200개의 캐시 항목만 저장
    }
}
