package com.jolupbisang.demo.application.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.common.validator.MeetingAccessValidator;
import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import com.jolupbisang.demo.application.meeting.exception.AudioError;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.meeting.audio.AudioRepository;
import com.jolupbisang.demo.infrastructure.meeting.client.WhisperClient;
import com.jolupbisang.demo.infrastructure.meeting.session.MeetingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AudioService {

    private final AudioRepository audioRepository;
    private final WhisperClient whisperClient;
    private final ObjectMapper objectMapper;
    private final MeetingAccessValidator meetingAccessValidator;
    private final MeetingSessionRepository meetingSessionRepository;

    public void registerSessionAndValidateAccess(WebSocketSession session, Long meetingId, Long userId) {
        Optional<WebSocketSession> existingSessionOpt = meetingSessionRepository.findByUserId(userId);

        if (existingSessionOpt.isPresent()) {
            closeAndRemoveExistingSession(existingSessionOpt.get(), userId);
        }

        meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);
        meetingSessionRepository.save(session, userId, meetingId);
    }

    public void unregisterSession(WebSocketSession session) {
        meetingSessionRepository.delete(session);
    }

    public void processAndSaveAudioData(WebSocketSession session, BinaryMessage message) throws IOException {
        long userId = meetingSessionRepository.getUserIdBySession(session)
                .orElseThrow(() -> new CustomException(AudioError.SESSION_INFO_NOT_FOUND));

        long meetingId = meetingSessionRepository.getMeetingIdBySession(session)
                .orElseThrow(() -> new CustomException(AudioError.SESSION_INFO_NOT_FOUND));

        ByteBuffer buffer = message.getPayload();
        AudioMeta audioMetaResult = getAudioMeta(buffer, userId, meetingId);
        byte[] audioData = extractAudioData(buffer);

        audioRepository.save(audioMetaResult, audioData);
        whisperClient.send(message);
    }

    private AudioMeta getAudioMeta(ByteBuffer byteBuffer, long userId, long meetingId) {
        AudioDetails audioDetails = extractAudioDetails(byteBuffer);
        return audioDetails.toAudioMeta(userId, meetingId);
    }

    private AudioDetails extractAudioDetails(ByteBuffer byteBuffer) {
        int metaLength = byteBuffer.getInt();
        if (metaLength <= 0 || metaLength > byteBuffer.remaining()) {
            throw new CustomException(AudioError.METADATA_INVALID_PAYLOAD_LENGTH);
        }
        byte[] metaDataBytes = new byte[metaLength];
        byteBuffer.get(metaDataBytes);
        String metaString = new String(metaDataBytes, StandardCharsets.UTF_8);

        try {
            return objectMapper.readValue(metaString, AudioDetails.class);
        } catch (JsonProcessingException ex) {
            if (ex.getCause() instanceof CustomException) {
                throw (CustomException) ex.getCause();
            }
            throw new CustomException(AudioError.INVALID_META_DATA);
        }
    }

    private byte[] extractAudioData(ByteBuffer byteBuffer) {
        byte[] audioBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(audioBytes);
        return audioBytes;
    }

    private record AudioDetails(String type, Long chunkId, String encoding, LocalDateTime timestamp) {
        public AudioDetails {
            if (type == null || type.trim().isEmpty()) {
                throw new CustomException(AudioError.METADATA_TYPE_INVALID);
            }
            if (chunkId == null) {
                throw new CustomException(AudioError.METADATA_CHUNKID_NULL);
            }
            if (encoding == null || encoding.trim().isEmpty()) {
                throw new CustomException(AudioError.METADATA_ENCODING_INVALID);
            }
            if (timestamp == null) {
                throw new CustomException(AudioError.METADATA_TIMESTAMP_NULL);
            }
        }

        public AudioMeta toAudioMeta(long userId, long meetingId) {
            return new AudioMeta(
                    this.type,
                    userId,
                    meetingId,
                    this.chunkId,
                    this.timestamp,
                    this.encoding
            );
        }
    }

    private void closeAndRemoveExistingSession(WebSocketSession existingSession, Long userId) {
        log.warn("User {} already has an active session {}. Closing the old session.", userId, existingSession.getId());
        try {
            existingSession.close(CloseStatus.POLICY_VIOLATION.withReason("New connection established"));
        } catch (IOException e) {
            log.error("Error closing existing WebSocket session {} for user {}: {}", existingSession.getId(), userId, e.getMessage());
        }
        meetingSessionRepository.deleteByUserId(userId);
    }
} 
