package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.service.AudioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketHandler extends BinaryWebSocketHandler {

    private final AudioService audioService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[{}] WebSocket Connection Established", session.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        audioService.processAndSaveAudioData(session, message);
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
}
