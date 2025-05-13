package com.jolupbisang.demo.presentation.auth.filter;


import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.global.exception.GlobalErrorCode;
import com.jolupbisang.demo.infrastructure.auth.JwtProvider;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    private static final String TOKEN_TYPE = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authToken == null || !authToken.startsWith(TOKEN_TYPE)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(authToken);

        Authentication authentication = getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String resolveAccessToken(String authToken) {
        String accessToken = authToken.substring(TOKEN_TYPE.length()).trim();

        if (jwtProvider.isExpired(accessToken)) {
            throw new CustomException(GlobalErrorCode.EXPIRED_JWT);
        }

        return accessToken;
    }

    private Authentication getAuthentication(String token) {
        Long userId = jwtProvider.getUserId(token);
        String email = jwtProvider.getEmail(token);
        String nickname = jwtProvider.getNickname(token);

        UserDetails userDetails = new CustomUserDetails(userId, email, nickname);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
