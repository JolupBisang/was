package com.jolupbisang.demo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.infrastructure.auth.JwtProvider;
import com.jolupbisang.demo.presentation.auth.filter.JwtAuthenticationFilter;
import com.jolupbisang.demo.presentation.auth.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterConfig {
    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService, jwtProvider);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(objectMapper);
    }
}

