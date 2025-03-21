package com.jolupbisang.demo.global.security.filter;


import com.jolupbisang.demo.global.common.exception.CustomException;
import com.jolupbisang.demo.global.common.exception.GlobalErrorCode;
import com.jolupbisang.demo.global.security.jwt.JwtProvider;
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
    private final JwtProvider JwtProvider;

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

        if (JwtProvider.isExpired(accessToken)) {
            throw new CustomException(GlobalErrorCode.EXPIRED_JWT);
        }

        return accessToken;
    }

    private Authentication getAuthentication(String token) {
        String userId = String.valueOf(JwtProvider.getUserId(token));

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
