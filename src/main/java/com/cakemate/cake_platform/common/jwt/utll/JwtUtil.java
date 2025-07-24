package com.cakemate.cake_platform.common.jwt.utll;

import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.store.owner.exception.ForbiddenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final int TOKEN_EXPIRATION_TIME;
    private final String BEARER_PREFIX = "Bearer ";
    // 생성, 검증, claims 추출에 사용하여 속성값에 사용
    private final SecretKey secretKey;
    private Date date;

    // 환경변수로 설정한 키 가져오는 속성값
    public JwtUtil(@Value("${JWT_SECRET_KEY}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.TOKEN_EXPIRATION_TIME = 1000 * 60 * 120;
    }

    public String createMemberJwtToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_EXPIRATION_TIME);

        if (hasOwnerId(member)) {
            String subjectOwnerId = member.getOwner().getId().toString();
            String bearerOwnerJwtToken = BEARER_PREFIX + Jwts.builder()
                    .subject(subjectOwnerId)
                    .issuedAt(now)
                    .claim("email", member.getOwner().getEmail())
                    .claim("memberType", "OWNER")
                    .expiration(expiration) // 만료시간 120분
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();
            return bearerOwnerJwtToken;
        } else {
            String subjectCustomerId = member.getCustomer().getId().toString();
            String bearerCustomerJwtToken = BEARER_PREFIX + Jwts.builder()
                    .subject(subjectCustomerId)
                    .issuedAt(now)
                    .claim("email", member.getCustomer().getEmail())
                    .claim("memberType", "CUSTOMER")
                    .expiration(expiration) // 만료시간 120분
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();
            return bearerCustomerJwtToken;
        }

    }
    //점주인지 소비자인지 구분하는 메서드
    public boolean isOwnerToken(Claims claims) {
        return "OWNER".equals(claims.get("memberType", String.class));
    }

    public boolean isCustomerToken(Claims claims) {
        return "CUSTOMER".equals(claims.get("memberType", String.class));
    }

    private boolean hasOwnerId(Member member) {
        if (member.getOwner() != null) {
            return true;
        } else {
            return false;
        }
    }

    // 추후 관리자 auth 생길 시 사용 할 예정
    private boolean hasCustomerId(Member member) {
        if (member.getCustomer() != null) {
            return true;
        } else {
            return false;
        }
    }

    // "Bearer 토큰"을 "토큰" 으로 반환시켜주는 로직
    public String substringToken(String bearerJwtToken) {
        /**
         * hasText -> bearerJWTToken = null , ""(빈 문자열), " "(공백의 경우) false
         * startsWith - BEARER_PREFIX( bearer ) 시작이 아니면 false
         * 즉 둘중에 하나라도 false 발생시 예외 발생
         */
        if (bearerJwtToken != null && !bearerJwtToken.isBlank() && bearerJwtToken.startsWith(BEARER_PREFIX)) {
            String substring = bearerJwtToken.substring(7);
            return substring;
        }

        throw new RuntimeException();
    }

    public Claims verifyToken(String JwtToken) {
        // 토큰 검증 로직
        Claims claims;
        claims = Jwts.parser() // 토큰 분석 준비
                .verifyWith(secretKey) // 비밀키 검증
                .build() //파서(분석기) 제작
                .parseSignedClaims(JwtToken) // 발급한 토큰을 분석, .claim을 검증하고 읽음 유효하면 claim 반환
                .getPayload(); // claim 객체에서 페이로드 추출
        return claims;
    }

    public Long subjectMemberId(Claims claims) {
        long memberId = Long.parseLong(claims.getSubject());
        return memberId;
    }
    //OwnerController에 사용
    public Long extractOwnerId(String authorizationHeader) {
        String token = substringToken(authorizationHeader);
        Claims claims = verifyToken(token);

        if (!isOwnerToken(claims)) {
            throw new ForbiddenException("점주 권한이 필요한 요청입니다.");
        }

        return subjectMemberId(claims);
    }
    //CustomerController에 사용
    public Long extractCustomerId(String authorizationHeader) {
        String token = substringToken(authorizationHeader);
        Claims claims = verifyToken(token);

        if (!isCustomerToken(claims)) {
            throw new ForbiddenException("고객 권한이 필요한 요청입니다.");
        }

        return subjectMemberId(claims);
    }
}
