package com.jolupbisang.demo.presentation.audio.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * WebSocket을 통해 전송되는 바이너리 오디오 메시지를 파싱하는 클래스
 * 메시지 구조:
 * [4바이트: 메타데이터 길이][N바이트: JSON 메타데이터][나머지: 오디오 데이터]
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AudioBinaryMessageParser {

    private static final int METADATA_LENGTH_SIZE = 4;

    private final ObjectMapper objectMapper;

    public AudioChunkDto parse(BinaryMessage message) {
        ByteBuffer buffer = message.getPayload();

        int metadataLength = extractMetadataLength(buffer);
        AudioMetadata metadata = extractAndParseMetadata(buffer, metadataLength);
        byte[] audioData = extractAudioData(buffer);

        return new AudioChunkDto(
                metadata.type(),
                metadata.chunkId(),
                metadata.encoding(),
                metadata.timestamp(),
                audioData
        );
    }

    private int extractMetadataLength(ByteBuffer buffer) {
        if (buffer.remaining() < METADATA_LENGTH_SIZE) {
            throw new IllegalArgumentException("Invalid payload: metadata length header missing");
        }

        int length = buffer.getInt();

        if (length <= 0 || length > buffer.remaining()) {
            log.error("Invalid metadata length: {}. Buffer remaining: {}", length, buffer.remaining());
            throw new IllegalArgumentException("Invalid metadata length: " + length);
        }

        return length;
    }

    private AudioMetadata extractAndParseMetadata(ByteBuffer buffer, int metadataLength) {
        byte[] metadataBytes = new byte[metadataLength];
        buffer.get(metadataBytes);

        String metadataJson = new String(metadataBytes, StandardCharsets.UTF_8);

        try {
            return objectMapper.readValue(metadataJson, AudioMetadata.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse metadata JSON: {}", metadataJson, e);
            throw new IllegalArgumentException("Invalid metadata JSON format", e);
        }
    }

    private byte[] extractAudioData(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            throw new IllegalArgumentException("Empty audio data");
        }

        byte[] audioData = new byte[buffer.remaining()];
        buffer.get(audioData);
        return audioData;
    }


    private record AudioMetadata(
            String type,
            long chunkId,
            String encoding,
            LocalDateTime timestamp
    ) {
    }
}

