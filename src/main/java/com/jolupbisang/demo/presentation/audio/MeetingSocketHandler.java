package com.jolupbisang.demo.presentation.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.audio.service.AudioService;
import com.jolupbisang.demo.global.exception.WebSocketErrorHandler;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponse;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketHandler extends AbstractWebSocketHandler {

    private final AudioService audioService;
    private final MeetingSocketDispatcher meetingSocketDispatcher;
    private final WebSocketErrorHandler webSocketErrorHandler;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            Long meetingId = extractMeetingIdFromUri(session);
            CustomUserDetails userDetails = (CustomUserDetails) session.getAttributes().get("userDetails");

            if (userDetails == null) {
                log.warn("[{}] User details not found in session. Closing session.", session.getId());
                session.close(CloseStatus.POLICY_VIOLATION.withReason("User details not found"));
                return;
            }

            Long userId = userDetails.getUserId();
            long lastProcessedChunkId = audioService.processSessionStartAndGetLastProcessedChunkId(session, userId, meetingId);

            SocketResponse<Long> socketResponse = SocketResponse.of(SocketResponseType.LAST_PROCESSED_CHUNK_ID, lastProcessedChunkId);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(socketResponse)));

        } catch (Exception ex) {
            webSocketErrorHandler.handleWebSocketError(session, ex);
            try {
                log.warn("[{}] Closing session due to exception during connection establishment: {}", session.getId(), ex.getMessage());
                session.close(CloseStatus.POLICY_VIOLATION);
            } catch (Exception closeEx) {
                log.error("[{}] Error closing WebSocket session after handling initial error: {}", session.getId(), closeEx.getMessage(), closeEx);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            meetingSocketDispatcher.dispatchTextMessage(session, message.getPayload());
        } catch (Exception ex) {
            webSocketErrorHandler.handleWebSocketError(session, ex);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            meetingSocketDispatcher.dispatchBinaryMessage(session, message);
        } catch (Exception ex) {
            webSocketErrorHandler.handleWebSocketError(session, ex);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("[{}] WebSocket Transport Error: {}", session.getId(), exception.getMessage());
        webSocketErrorHandler.handleWebSocketError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            audioService.unregisterSession(session);
        } catch (Exception ex) {
            log.error("[{}] Exception during session unregistration via AudioService on connection closed. Status: {}",
                    session.getId(), status, ex);
        }
        log.debug("[{}] WebSocket Connection Closed - Status: {}. Session unregistration attempted.", session.getId(), status);
    }

    private Long extractMeetingIdFromUri(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) {
            throw new IllegalStateException("WebSocket URI is null.");
        }

        String path = uri.getPath();
        if (path == null || !path.startsWith("/ws/meeting/audio/")) {
            throw new IllegalArgumentException("Invalid WebSocket path format.");
        }

        String meetingIdStr = path.substring("/ws/meeting/audio/".length());
        try {
            return Long.parseLong(meetingIdStr);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Invalid meeting ID format: " + meetingIdStr);
        }
    }
}
