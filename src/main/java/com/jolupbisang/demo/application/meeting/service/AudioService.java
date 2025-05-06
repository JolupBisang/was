package com.jolupbisang.demo.application.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import com.jolupbisang.demo.application.meeting.exception.AudioError;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.meeting.audio.AudioRepository;
import com.jolupbisang.demo.infrastructure.meeting.client.WhisperClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioService {

    private final AudioRepository audioRepository;
    private final WhisperClient whisperClient;
    private final ObjectMapper objectMapper;

    public void processAndSaveAudioData(WebSocketSession session, BinaryMessage message) throws IOException {
        ByteBuffer buffer = message.getPayload();
        AudioMeta audioMeta = extractAudioMeta(buffer);
        byte[] audioData = extractAudioData(buffer);

        log.info("[{}] Audio accepted: userId: {}, meetingId: {}, chunkId: {}",
                session.getId(), audioMeta.userId(), audioMeta.meetingId(), audioMeta.chunkId());

        audioRepository.save(audioMeta, audioData);
        whisperClient.send(message);
    }

    private AudioMeta extractAudioMeta(ByteBuffer byteBuffer) {
        int metaLength = byteBuffer.getInt();
        byte[] metaData = new byte[metaLength];
        byteBuffer.get(metaData);
        String metaString = new String(metaData, StandardCharsets.UTF_8);

        try {
            return objectMapper.readValue(metaString, AudioMeta.class);
        } catch (JsonProcessingException ex) {
            throw new CustomException(AudioError.INVALID_META_DATA);
        }
    }

    private byte[] extractAudioData(ByteBuffer byteBuffer) {
        byte[] audioBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(audioBytes);
        return audioBytes;
    }
} 
