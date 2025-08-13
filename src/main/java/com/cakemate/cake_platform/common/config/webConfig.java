package com.cakemate.cake_platform.common.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig {
//    private final JwtUtil jwtUtil;
//
//    public webConfig(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    FilterRegistrationBean<Filter> addJWTFilter() {
//        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//        filterFilterRegistrationBean.setFilter();
//        filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
//        filterFilterRegistrationBean.addUrlPatterns("/*");
//        return filterFilterRegistrationBean;
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 모든 경로에 대해
                        .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500") // 프론트 주소 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                        .allowCredentials(true); // 쿠키/인증 정보 허용

            }
        };
    }
}
