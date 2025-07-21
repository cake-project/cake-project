package com.cakemate.cake_platform.common.jwt.filter;

import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public class jwtFilter implements Filter {
    private final JwtUtil jwtUtil;
    private final Set<String> WHTIE_lIST = Set.of(
            "/api/signup/*",
            "/api/signin/*"

    );

    public jwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String bearerJwtToken = httpRequest.getHeader("Authorization");
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
