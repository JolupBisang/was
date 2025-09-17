package com.jolupbisang.demo.global.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebSocketPathInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            attributes.putAll(extractPathVariables(request.getURI().getPath()));
            return true;
        } catch (Exception e) {
            log.warn("Failed to extract path variables from path: {}", request.getURI().getPath(), e);
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 필요시 구현
    }

    private Map<String, Object> extractPathVariables(String path) {
        Map<String, Object> variables = new HashMap<>();
        String[] segments = path.split("/");

        try {
            if (path.matches("/ws/v1/meeting/\\d+")) {
                variables.put("meetingId", segments[3]);
            } else {
                log.warn("Unknown WebSocket path pattern: {}", path);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Invalid path structure: {}", path, e);
            throw new IllegalArgumentException("Invalid path structure", e);
        }

        return variables;
    }
}
