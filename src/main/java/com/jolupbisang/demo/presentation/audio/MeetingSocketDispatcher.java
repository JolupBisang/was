package com.jolupbisang.demo.presentation.audio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.audio.command.AudioChunkReceiveService;
import com.jolupbisang.demo.application.audio.command.dto.AudioChunkReq;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.global.exception.GlobalErrorCode;
import com.jolupbisang.demo.presentation.audio.dto.request.SocketRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketDispatcher {

    private final ObjectMapper objectMapper;
    private final AudioChunkReceiveService audioChunkReceiveService;

    public void dispatchTextMessage(WebSocketSession session, String messagePayload) {
        try {
            SocketRequest requestMessage = objectMapper.readValue(messagePayload, SocketRequest.class);

            switch (requestMessage.type()) {
                case CANCEL_COMPLETION:
                    break;
                default:
                    log.warn("[{}] 처리할 수 없는 메시지 타입입니다: {}", session.getId(), requestMessage.type());
            }
        } catch (Exception e) {
            log.error("[{}] 메시지 처리 중 에러 발생: {}, 페이로드: {}", session.getId(), e.getMessage(), messagePayload, e);
        }
    }

    public void dispatchBinaryMessage(WebSocketSession session, BinaryMessage binaryMessage) {
        audioChunkReceiveService.receiveAudioChunk(
                (Long) session.getAttributes().get("meetingId"),
                (Long) session.getAttributes().get("userId"),
                extractAudioChunkReq(binaryMessage)
        );
    }

    private AudioChunkReq extractAudioChunkReq(BinaryMessage binaryMessage) {
        ByteBuffer buffer = binaryMessage.getPayload();
        AudioMeta audioMeta = extractAudioMeta(buffer);
        byte[] audioData = extractAudioData(buffer);

        return new AudioChunkReq(audioMeta.type, audioMeta.chunkId, audioMeta.encoding, audioMeta.timestamp, audioData);
    }

    private AudioMeta extractAudioMeta(ByteBuffer byteBuffer) {
        int metaLength = byteBuffer.getInt();
        if (metaLength <= 0 || metaLength > byteBuffer.remaining()) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT, "metaLength: %d", metaLength);
        }

        byte[] metaDataBytes = new byte[metaLength];
        byteBuffer.get(metaDataBytes);
        String metaString = new String(metaDataBytes, StandardCharsets.UTF_8);

        try {
            return objectMapper.readValue(metaString, AudioMeta.class);
        } catch (JsonProcessingException ex) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT, ex);
        }
    }

    private byte[] extractAudioData(ByteBuffer byteBuffer) {
        byte[] audioBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(audioBytes);
        return audioBytes;
    }

    private record AudioMeta(
            String type,
            long chunkId,
            String encoding,
            LocalDateTime timestamp
    ) {
    }
} 
