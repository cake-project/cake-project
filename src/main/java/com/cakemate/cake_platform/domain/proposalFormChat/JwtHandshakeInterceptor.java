package com.cakemate.cake_platform.domain.proposalFormChat;

import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtUtil jwtUtil;

    public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        //Authorization 헤더에서 Bearer 토큰 꺼내기
        List<String> authorizationHeader = request.getHeaders().get("Authorization");

        try {
            if (authorizationHeader == null || authorizationHeader.isEmpty()) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            //HTTP Authorization 헤더에는 여러 인증 스킴이 올 수 있으니,
            //Bearer 토큰만 허용하니까 "Bearer "로 시작하는지 먼저 검사 후
            //다른 인증 방식이면 거절
            String headerValue = authorizationHeader.get(0); //Authorization 헤더는 한 개만 존재해서 (0) 을 넘기면 인덱스 범위를 벗어나서 IndexOutOfBoundsException 발생
            if (headerValue == null || !headerValue.regionMatches(
                    true, 0, "Bearer ", 0, 7
            )) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            // "Bearer " 제거
            String token = jwtUtil.substringToken(headerValue);

            // 토큰 검증 및 Claims 추출
            Claims claims = jwtUtil.verifyToken(token);

            // memberId 추출
            Long memberId = jwtUtil.subjectMemberId(claims);

            //토큰 만료시간 추출
            long expirationTime = claims.getExpiration().getTime();

            //토큰에 저장된 memberName 추출
            String memberName = jwtUtil.extractMemberName(claims);

            boolean customerToken = jwtUtil.isCustomerToken(claims);

            boolean ownerToken = jwtUtil.isOwnerToken(claims);

            if (!customerToken && !ownerToken) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            if (ownerToken)    attributes.put("ownerId", memberId);
            if (customerToken) attributes.put("customerId", memberId);


            // 공통 식별자
            attributes.put("memberId", memberId);

            //토큰 만료시간 저장
            attributes.put("expirationTime", expirationTime);

            //이름 저장
            attributes.put("memberName", memberName);

            //그 토큰이 점주용 토큰인지(ownerToken), 소비자용 토큰인지(customerToken)를 판별 후
            //판별 결과를 문자열 "OWNER" 또는 "CUSTOMER"로 변환해서
            //session.getAttributes()에 memberType 키로 저장.
            attributes.put("memberType", ownerToken ? "OWNER" : "CUSTOMER");

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {

    }
}
