package com.jolupbisang.demo.infrastructure.meeting.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.event.whisper.WhisperContextEvent;
import com.jolupbisang.demo.application.event.whisper.WhisperDiarizedEvent;
import com.jolupbisang.demo.application.event.whisper.WhisperEmbeddedEvent;
import com.jolupbisang.demo.global.properties.WhisperProperties;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.request.*;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.ContextResponse;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.DiarizedResponse;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.EmbeddedVectorResponse;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.WhisperResponseType;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WhisperClient extends BinaryWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private WebSocketSession whisperSession;

    private final WhisperProperties whisperProperties;

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

            String flag = objectMapper.readTree(jsonResponse).path("flag").asText();

            if (WhisperResponseType.CONTEXT.getValue().equals(flag)) {
                processContextResponse(jsonResponse);
            } else if (WhisperResponseType.DIARIZED.getValue().equals(flag)) {
                processDiarizedResponse(jsonResponse);
            } else if (WhisperResponseType.EMBEDDED.getValue().equals(flag)) {
                byte[] audio = new byte[payload.remaining()];
                payload.get(audio);
                processEmbeddedResponse(jsonResponse, audio);
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

    public void sendEmbeddingAudio(long userId, byte[] audioData) {
        try {
            if (whisperSession != null && whisperSession.isOpen()) {
                EmbeddingRequest embeddingRequest = EmbeddingRequest.of(userId, audioData);
                whisperSession.sendMessage(embeddingRequest.toBinaryMessage(objectMapper));
            } else {
                log.warn("[WhisperClient] Whisper session is not open. Cannot send embedding audio for user ID: {}", userId);
            }
        } catch (IOException e) {
            log.error("[WhisperClient] Failed to send embedding audio for user ID: {}. Error: {}", userId, e.getMessage(), e);
        }
    }

    public void sendRefenceVector(long groupId, List<Long> userIds, List<Integer> counts, List<byte[]> vectors) {
        try {
            if (whisperSession != null && whisperSession.isOpen()) {
                ReferenceRequest referenceRequest = ReferenceRequest.of(groupId, userIds, counts, vectors);
                whisperSession.sendMessage(referenceRequest.toBinaryMessage(objectMapper));
            } else {
                log.warn("[WhisperClient] Whisper session is not open. Cannot send embedded vector for groupx ID: {}", groupId);
            }
        } catch (IOException e) {
            log.error("[WhisperClient] Failed to send embedding audio for group ID: {}. Error: {}", groupId, e.getMessage(), e);
        }
    }

    private void connectToWhisperServer() {
        WebSocketClient client = new StandardWebSocketClient();
        try {
            whisperSession = client.execute(this, whisperProperties.getWebsocketUrl()).get();
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

    private void processEmbeddedResponse(String jsonResponse, byte[] audio) throws IOException {
        EmbeddedVectorResponse embeddedVectorResponse = objectMapper.readValue(jsonResponse, EmbeddedVectorResponse.class).withAudio(audio);
        this.eventPublisher.publishEvent(new WhisperEmbeddedEvent(embeddedVectorResponse));
    }
}
