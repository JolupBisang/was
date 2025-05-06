package com.jolupbisang.demo.application.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import com.jolupbisang.demo.application.meeting.exception.AudioError;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.global.properties.MeetingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioService {

    private final MeetingProperties meetingProperties;
    private final ObjectMapper objectMapper;

    public void processAndSaveAudioData(WebSocketSession session, BinaryMessage message) throws IOException {
        ByteBuffer buffer = message.getPayload();
        AudioMeta audioMeta = extractAudioMeta(buffer);
        byte[] audioData = extractAudioData(buffer);

        log.info("[{}] Audio accepted: userId: {}, meetingId: {}, chunkId: {}",
                session.getId(), audioMeta.userId(), audioMeta.meetingId(), audioMeta.chunkId());

        saveAudio(audioMeta, audioData);
    }

    private void saveAudio(AudioMeta audioMeta, byte[] audioData) throws IOException {
        Path dirPath = Path.of(meetingProperties.getBaseDir(),
                Long.toString(audioMeta.meetingId()),
                Long.toString(audioMeta.userId()));
        String filename = Integer.toString(audioMeta.chunkId());

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        File chunkFile = dirPath.resolve(filename).toFile();

        try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
            fos.write(audioData);
        } catch (IOException e) {
            log.error("[Failed to save audio file] meetingId: {}, userId:{}, chunkId:{} ", audioMeta.meetingId(), audioMeta.userId(), audioMeta.chunkId(), e);
            throw e;
        }
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
