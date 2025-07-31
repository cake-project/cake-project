package com.cakemate.cake_platform.common.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtConfig {
    @Value("${JWT_SECRET_KEY}")
    private String rawSecret;          // .properties 또는 환경변수에 저장된 64byte 이상 문자열

    @Bean
    public SecretKey jwtSecretKey() {  // 메서드 이름은 임의
        // HS256용 256bit 이상 비밀키 생성
        return Keys.hmacShaKeyFor(rawSecret.getBytes(StandardCharsets.UTF_8));
    }
}
