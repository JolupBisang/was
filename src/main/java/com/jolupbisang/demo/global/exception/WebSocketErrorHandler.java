package com.jolupbisang.demo.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.global.response.ErrorResponse;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketErrorHandler {

    private final ObjectMapper objectMapper;

    public void handleWebSocketError(WebSocketSession session, Throwable exception) {
        String errorId = UUID.randomUUID().toString();
        ErrorCode errorCode = resolveErrorCode(exception);
        String clientMessage = errorCode.getMessage();
        
        log.error("[WebSocket Error - ErrorId: {}, Session: {}] ResolvedMessage: {}",
                errorId, session.getId(), clientMessage, exception);

        if (session.isOpen()) {
            try {
                SocketResponse<ErrorResponse> socketErrorResponse = SocketResponse.error(clientMessage, errorId);
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(socketErrorResponse)));
            } catch (Exception e) {
                log.error("[WebSocket Send Fail - ErrorId: {}, Session: {}] Failed to send error response to client, SendExMsg: {}",
                    errorId, session.getId(), exception.getMessage(), e);
            }
        }
    }

    private ErrorCode resolveErrorCode(Throwable exception) {
        if (exception instanceof CustomException customEx) {
            return customEx.getErrorCode();
        } else if (exception instanceof IOException) {
            return GlobalErrorCode.INTERNAL_SERVER_ERROR;
        }

        return GlobalErrorCode.INTERNAL_SERVER_ERROR;
    }
} 
