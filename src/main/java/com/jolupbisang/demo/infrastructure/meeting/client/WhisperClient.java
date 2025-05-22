package com.jolupbisang.demo.infrastructure.meeting.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.ContextDoneRequest;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.ContextRequest;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.DiarizedRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WhisperClient extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private WebSocketSession whisperSession;

    private static final String WHISPER_WEBSOCKET_URL = "wss://your-whisper-server-url/ws";

    @PostConstruct
    public void init() {
        connectToWhisperServer();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("[WhisperClient] Whisper WebSocket connection established");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //todo: whisper response 처리
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Whisper WebSocket connection closed with status: {}", status);
        connectToWhisperServer();
    }

    public void sendDiarized(long meetingId, long userId, byte[] audioData) {
        try {
            if (whisperSession != null && whisperSession.isOpen()) {
                DiarizedRequest request = DiarizedRequest.of(String.valueOf(meetingId), String.valueOf(userId), audioData);
                whisperSession.sendMessage(request.toBinary(objectMapper));
            }
        } catch (IOException e) {
            log.error("[WhisperClient] Failed to send diarized request", e);
        }
    }

    public void sendContext(long meetingId) {
        try {
            if (whisperSession != null && whisperSession.isOpen()) {
                ContextRequest request = ContextRequest.of(String.valueOf(meetingId));
                whisperSession.sendMessage(request.toTextMessage(objectMapper));
            } else {
                log.error("[WhisperClient] Whisper WebSocket connection is not available");
            }
        } catch (IOException e) {
            log.error("[WhisperClient] Failed to send context request to Whisper server", e);
        }
    }

    public void sendContextDone(long meetingId) {
        try {
            if (whisperSession != null && whisperSession.isOpen()) {
                ContextDoneRequest request = ContextDoneRequest.of(String.valueOf(meetingId));
                whisperSession.sendMessage(request.toTextMessage(objectMapper));
            } else {
                log.error("[WhisperClient] Whisper WebSocket connection is not available");
            }
        } catch (IOException e) {
            log.error("[WhisperClient] Failed to send context_done request to Whisper server", e);
        }
    }

    private void connectToWhisperServer() {
        WebSocketClient client = new StandardWebSocketClient();
        try {
            whisperSession = client.execute(this, WHISPER_WEBSOCKET_URL).get();
            log.info("[WhisperClient] Connected to Whisper server");
        } catch (InterruptedException | ExecutionException e) {
            log.error("[WhisperClient] Failed to connect to Whisper server", e);
        }
    }
}
