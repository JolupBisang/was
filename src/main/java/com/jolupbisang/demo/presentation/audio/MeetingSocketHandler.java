package com.jolupbisang.demo.presentation.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.audio.service.AudioService;
import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.meeting.service.MeetingService;
import com.jolupbisang.demo.global.exception.WebSocketErrorHandler;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.audio.dto.response.ConnectionEstablishedRes;
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
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketHandler extends AbstractWebSocketHandler {

    private final AudioService audioService;
    private final MeetingService meetingService;
    private final MeetingSocketDispatcher meetingSocketDispatcher;
    private final WebSocketErrorHandler webSocketErrorHandler;
    private final MeetingAccessValidator meetingAccessValidator;

    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            long meetingId = extractMeetingId(session);
            long userId = extractUserId(session);

            meetingAccessValidator.validateMeetingIsInProgress(meetingId);
            long lastProcessedChunkId = audioService.processSessionStartAndGetLastProcessedChunkId(session, userId, meetingId);
            LocalDateTime meetingStartTime = meetingService.getMeetingStartTime(meetingId);
            ConnectionEstablishedRes connectionEstablishedRes = ConnectionEstablishedRes.of(lastProcessedChunkId, meetingStartTime);

            SocketResponse<?> socketResponse = SocketResponse.of(SocketResponseType.CONNECTION_ESTABLISHED, connectionEstablishedRes);
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


    private long extractUserId(WebSocketSession session) {
        CustomUserDetails userDetails = (CustomUserDetails) session.getAttributes().get("userDetails");
        return userDetails.getUserId();
    }

    private long extractMeetingId(WebSocketSession session) {
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
