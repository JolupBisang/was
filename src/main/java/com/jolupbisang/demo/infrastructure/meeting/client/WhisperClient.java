package com.jolupbisang.demo.infrastructure.meeting.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.event.whisper.WhisperContextEvent;
import com.jolupbisang.demo.application.event.whisper.WhisperDiarizedEvent;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.request.ContextDoneRequest;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.request.ContextRequest;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.request.DiarizedRequest;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.ContextResponse;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.DiarizedResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WhisperClient extends BinaryWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
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
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            ByteBuffer payload = message.getPayload();

            int jsonLength = readJsonLength(payload);
            if (jsonLength == -1) {
                return;
            }

            String jsonResponse = readJsonPayload(payload, jsonLength);
            if (jsonResponse == null) {
                return;
            }

            if (jsonResponse.contains("\"context\"")) {
                processContextResponse(jsonResponse);
            } else if (jsonResponse.contains("\"completed\"")) {
                processDiarizedResponse(jsonResponse);
            } else {
                log.warn("[WhisperClient] Unknown message type received: {}", jsonResponse);
            }
        } catch (Exception e) {
            log.error("[WhisperClient] Error handling binary message", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Whisper WebSocket connection closed with status: {}", status);
        connectToWhisperServer();
    }

    public void sendDiarization(long meetingId, long userId, Integer scOffset, byte[] audioData) {
        try {
            if (whisperSession != null && whisperSession.isOpen()) {
                DiarizedRequest request = DiarizedRequest.of(meetingId, userId, scOffset, audioData);
                whisperSession.sendMessage(request.toBinaryMessage(objectMapper));
            }
        } catch (IOException e) {
            log.error("[WhisperClient] Failed to send diarized request", e);
        }
    }

    public void sendContext(long meetingId) {
        try {
            if (whisperSession != null && whisperSession.isOpen()) {
                ContextRequest request = ContextRequest.of(meetingId);
                whisperSession.sendMessage(request.toBinaryMessage(objectMapper));
            }
        } catch (IOException e) {
            log.error("[WhisperClient] Failed to send context request to Whisper server", e);
        }
    }

    public void sendContextDone(long meetingId) {
        try {
            if (whisperSession != null && whisperSession.isOpen()) {
                ContextDoneRequest request = ContextDoneRequest.of(meetingId);
                whisperSession.sendMessage(request.toBinaryMessage(objectMapper));
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


    private int readJsonLength(ByteBuffer payload) {
        if (payload.remaining() < 4) {
            log.warn("[WhisperClient] Received message too short to contain length prefix");
            return -1;
        }
        return payload.getInt();
    }

    private String readJsonPayload(ByteBuffer payload, int jsonLength) {
        if (payload.remaining() < jsonLength) {
            log.warn("[WhisperClient] Received message shorter than specified JSON length. Expected: {}, Actual: {}", jsonLength, payload.remaining());
            return null;
        }
        byte[] jsonBytes = new byte[jsonLength];
        payload.get(jsonBytes);

        return new String(jsonBytes, StandardCharsets.UTF_8);
    }

    private void processContextResponse(String jsonResponse) throws IOException {
        ContextResponse contextResponse = objectMapper.readValue(jsonResponse, ContextResponse.class);
        this.eventPublisher.publishEvent(new WhisperContextEvent(contextResponse));
    }

    private void processDiarizedResponse(String jsonResponse) throws IOException {
        DiarizedResponse diarizedResponse = objectMapper.readValue(jsonResponse, DiarizedResponse.class);
        this.eventPublisher.publishEvent(new WhisperDiarizedEvent(diarizedResponse));
    }
}
