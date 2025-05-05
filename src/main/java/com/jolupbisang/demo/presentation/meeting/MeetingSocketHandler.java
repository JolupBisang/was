package com.jolupbisang.demo.presentation.meeting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.meeting.service.AudioService;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.global.exception.GlobalErrorCode;
import com.jolupbisang.demo.global.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketHandler extends BinaryWebSocketHandler {

    private final AudioService audioService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[{}] WebSocket Connection Established", session.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        try {
            audioService.processAndSaveAudioData(session, message);
        } catch (Exception ex) {
            handleExceptionAndNotifyClient(session, ex);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.sendMessage(new TextMessage("Error: " + exception.getMessage()));
        log.info("[{}] WebSocket Transport Error: {}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("[{}] WebSocket Connection Closed - Status: {}", session.getId(), status);
    }

    private void handleExceptionAndNotifyClient(WebSocketSession session, Exception ex) {
        log.error("[{}] Exception during WebSocket operation: {}", session.getId(), ex.getMessage(), ex);

        try {
            if (session.isOpen()) {
                sendErrorResponse(session, ex);
                log.debug("[{}] Error message sent to client", session.getId());
            } else {
                log.debug("[{}] Session closed, cannot send error message", session.getId());
            }
        } catch (Exception sendEx) {
            log.error("[{}] Failed to send error message: {}", session.getId(), sendEx.getMessage());
        }
    }

    private void sendErrorResponse(WebSocketSession session, Exception ex) throws IOException {
        String errorId = UUID.randomUUID().toString();
        ErrorResponse errorResponse = buildErrorResponse(ex, errorId);

        log.error("[{}] Exception occurred: {}", errorId, errorResponse.message(), ex);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(errorResponse)));
    }

    private ErrorResponse buildErrorResponse(Exception ex, String errorId) {
        if (ex instanceof CustomException) {
            return ErrorResponse.of(((CustomException) ex).getErrorCode().getMessage(), errorId);
        }
        return ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage(), errorId);
    }
}
