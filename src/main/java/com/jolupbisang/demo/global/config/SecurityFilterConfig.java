package com.jolupbisang.demo.global.config;

import com.jolupbisang.demo.global.security.filter.JwtAuthenticationFilter;
import com.jolupbisang.demo.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterConfig {
    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService, jwtProvider);
    }
}

