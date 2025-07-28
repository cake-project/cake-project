package com.cakemate.cake_platform.common.jwt.filter;

import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;
    private final Set<String> WHITE_LIST = Set.of(
            "/api/signup/*",
            "/api/signin/*"

    );

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        // 화이트리스트 경로는 토큰 검사 없이 통과
        if (WHITE_LIST.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        try {
            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing");
                return;
            }

            // "Bearer " 제거
            String token = jwtUtil.substringToken(authorizationHeader);

            // 토큰 검증 및 Claims 추출
            Claims claims = jwtUtil.verifyToken(token);

            // memberId 추출
            Long memberId = jwtUtil.subjectMemberId(claims);

            // 토큰 타입에 따라 request attribute 설정
            if (jwtUtil.isOwnerToken(claims)) {
                request.setAttribute("ownerId", memberId);
            } else if (jwtUtil.isCustomerToken(claims)) {
                request.setAttribute("customerId", memberId);
            } else {
                // 토큰 타입이 OWNER도 CUSTOMER도 아닐 경우 접근 거부
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token type");
                return;
            }

            // 다음 필터/컨트롤러로 진행
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (JwtException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        }
    }
}
