package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.service.AudioService;
import com.jolupbisang.demo.global.exception.WebSocketErrorHandler;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketHandler extends BinaryWebSocketHandler {

    private final AudioService audioService;
    private final WebSocketErrorHandler webSocketErrorHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            Long meetingId = extractMeetingIdFromUri(session);
            CustomUserDetails userDetails = (CustomUserDetails) session.getAttributes().get("userDetails");

            if (userDetails == null) {
                throw new IllegalStateException("User details not found in session");
            }

            Long userId = userDetails.getUserId();

            log.info("[{}] Attempting to register session: userId={}, meetingId={}", session.getId(), userId, meetingId);
            audioService.registerSessionAndValidateAccess(session, meetingId, userId);
            log.info("[{}] Session registration successful: userId={}, meetingId={}", session.getId(), userId, meetingId);

        } catch (Exception ex) {
            webSocketErrorHandler.handleWebSocketError(session, ex);
            try {
                log.warn("[{}] Closing session due to exception during connection establishment: {}", session.getId(), ex.getMessage());
                session.close(CloseStatus.NORMAL);
            } catch (Exception closeEx) {
                log.error("[{}] Error closing WebSocket session after handling initial error: {}", session.getId(), closeEx.getMessage(), closeEx);
            }
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            audioService.processAndSaveAudioData(session, message);
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
