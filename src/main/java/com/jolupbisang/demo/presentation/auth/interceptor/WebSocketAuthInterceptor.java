package com.jolupbisang.demo.presentation.auth.interceptor;

import com.jolupbisang.demo.infrastructure.auth.JwtProvider;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;
    private static final String TOKEN_PARAM = "token";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (!(request instanceof ServletServerHttpRequest)) {
            return false;
        }

        String token = extractToken(request);
        if (token == null) {
            return false;
        }

        try {
            CustomUserDetails userDetails = getUserDetails(token);
            attributes.put("userDetails", userDetails);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        //필요시 작성
    }

    private String extractToken(ServerHttpRequest request) {
        URI uri = request.getURI();

        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .getFirst(TOKEN_PARAM);
    }

    private CustomUserDetails getUserDetails(String token) {
        Long userId = jwtProvider.getUserId(token);
        String email = jwtProvider.getEmail(token);
        String nickname = jwtProvider.getNickname(token);

        return new CustomUserDetails(userId, email, nickname);
    }
} 
