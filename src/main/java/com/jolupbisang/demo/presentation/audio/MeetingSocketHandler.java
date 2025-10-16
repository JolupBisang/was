package com.jolupbisang.demo.presentation.audio;

import com.jolupbisang.demo.application.meeting.Intergration.MeetingWebsocketConnectionService;
import com.jolupbisang.demo.application.meeting.event.MeetingSessionClosedEvent;
import com.jolupbisang.demo.global.event.Events;
import com.jolupbisang.demo.global.websocket.WebSocketErrorHandler;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketHandler extends AbstractWebSocketHandler {

    private final MeetingSocketDispatcher meetingSocketDispatcher;
    private final MeetingWebsocketConnectionService meetingWebsocketConnectionService;
    private final WebSocketErrorHandler webSocketErrorHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        Long tmpMeetingId = 0L, tmpUserId = 0L;

        Map<String, Object> attributes = session.getAttributes();
        for (String key : attributes.keySet()) {
            log.debug("[{}] Session attribute: {}, {}", session.getId(), key, attributes.get(key));
        }

        try {
            CustomUserDetails userDetails = (CustomUserDetails) session.getAttributes().get("userDetails");
            tmpMeetingId = Long.parseLong((String) session.getAttributes().get("meetingId"));
            tmpUserId = userDetails.getUserId();

            log.debug("userId : {}, meetingId: {}", userDetails.getUserId(), tmpMeetingId);

            if (tmpMeetingId == null || tmpUserId == null) {
                log.info("[{}] Missing userId or meetingId in session attributes", session.getId());
                throw new IllegalArgumentException("Missing userId or meetingId in session attributes");
            }
        } catch (Exception ex) {
            log.info("Error processing WebSocket handshake: sessionId: {}, message = {}", session.getId(), ex.getMessage(), ex);
            closeQuietly(session);
        }

        long meetingId = tmpMeetingId;
        long userId = tmpUserId;

        if (!webSocketErrorHandler.handleWithErrorManagement(session, () ->
                meetingWebsocketConnectionService.registerSessionToMeeting(session, meetingId, userId))) {
            closeQuietly(session);
        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        webSocketErrorHandler.handleWithErrorManagement(session, () -> {
            meetingSocketDispatcher.dispatchTextMessage(session, message.getPayload());
        });
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        webSocketErrorHandler.handleWithErrorManagement(session, () -> {
            meetingSocketDispatcher.dispatchBinaryMessage(session, message);
        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("[{}] WebSocket Transport Error: {}", session.getId(), exception.getMessage(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long meetingId = (Long) session.getAttributes().get("meetingId");
        Long userId = (Long) session.getAttributes().get("userId");

        if (meetingId == null || userId == null) return;
        log.info("[{}] WebSocket Connection Closed - Status: {}. Session unregistration attempted.", session.getId(), status);

        Events.raise(new MeetingSessionClosedEvent(meetingId, userId));
    }

    private void closeQuietly(WebSocketSession session) {
        try {
            session.close(CloseStatus.POLICY_VIOLATION);
        } catch (Exception closeEx) {
            log.warn("[{}] Error closing WebSocket session: {}", session.getId(), closeEx.getMessage(), closeEx);
        }
    }
}
