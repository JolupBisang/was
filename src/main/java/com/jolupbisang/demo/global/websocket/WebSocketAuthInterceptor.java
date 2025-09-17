package com.jolupbisang.demo.global.websocket;

import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.global.exception.GlobalErrorCode;
import com.jolupbisang.demo.infrastructure.auth.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;

    private static final String TOKEN_TYPE = "Bearer ";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            String authToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authToken == null || !authToken.startsWith(TOKEN_TYPE)) {
                log.warn("Missing or invalid Authorization header in WebSocket handshake");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            String accessToken = resolveAccessToken(authToken);

            Long userId = jwtProvider.getUserId(accessToken);
            String email = jwtProvider.getEmail(accessToken);
            String nickname = jwtProvider.getNickname(accessToken);

            // 4. attributes에 사용자 정보 저장
            attributes.put("userId", userId);
            attributes.put("userEmail", email);
            attributes.put("userNickname", nickname);

            log.info("WebSocket authentication successful - userId: {}", userId);
            return true;

        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token in WebSocket handshake: {}", e.getMessage());
            response.setStatusCode(GlobalErrorCode.EXPIRED_JWT.getStatus());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token in WebSocket handshake: {}", e.getMessage());
            response.setStatusCode(GlobalErrorCode.INVALID_ACCESS_TOKEN.getStatus());
            return false;
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature in WebSocket handshake: {}", e.getMessage());
            response.setStatusCode(GlobalErrorCode.INVALID_TOKEN_SIGNATURE.getStatus());
            return false;
        } catch (JwtException e) {
            log.warn("Unknown JWT error in WebSocket handshake: {}", e.getMessage());
            response.setStatusCode(GlobalErrorCode.UNKNOWN_TOKEN_ERROR.getStatus());
            return false;
        } catch (CustomException e) {
            log.warn("Custom exception in WebSocket handshake: {}", e.getMessage());
            response.setStatusCode(HttpStatus.valueOf(e.getErrorCode().getStatus().value()));
            return false;
        } catch (Exception e) {
            log.error("Unexpected error in WebSocket handshake", e);
            response.setStatusCode(GlobalErrorCode.INTERNAL_SERVER_ERROR.getStatus());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("WebSocket handshake error occurred", exception);
        } else {
            log.debug("WebSocket handshake completed successfully");
        }
    }

    private String resolveAccessToken(String authToken) {
        String accessToken = authToken.substring(TOKEN_TYPE.length()).trim();

        if (jwtProvider.isExpired(accessToken)) {
            throw new CustomException(GlobalErrorCode.EXPIRED_JWT);
        }

        return accessToken;
    }

}
