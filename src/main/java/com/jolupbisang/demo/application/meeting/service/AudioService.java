package com.jolupbisang.demo.application.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.common.validator.MeetingAccessValidator;
import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import com.jolupbisang.demo.application.meeting.event.MeetingCompletedEvent;
import com.jolupbisang.demo.application.meeting.exception.AudioError;
import com.jolupbisang.demo.domain.meetingUser.MeetingUser;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.aws.s3.S3ClientUtil;
import com.jolupbisang.demo.infrastructure.meeting.audio.AudioRepository;
import com.jolupbisang.demo.infrastructure.meeting.client.WhisperClient;
import com.jolupbisang.demo.infrastructure.meeting.session.AudioProgressRepository;
import com.jolupbisang.demo.infrastructure.meeting.session.MeetingSessionRepository;
import com.jolupbisang.demo.infrastructure.meetingUser.MeetingUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AudioService {

    private final AudioRepository audioRepository;
    private final WhisperClient whisperClient;
    private final ObjectMapper objectMapper;
    private final MeetingAccessValidator meetingAccessValidator;
    private final MeetingSessionRepository meetingSessionRepository;
    private final AudioProgressRepository audioProgressRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final S3ClientUtil s3Client;

    @Transactional
    public long processSessionStartAndGetLastProcessedChunkId(WebSocketSession session, Long userId, Long meetingId) {
        registerSessionAndValidateAccess(session, meetingId, userId);

        return audioProgressRepository.findLastProcessedChunkId(userId, meetingId)
                .orElse(-1L);
    }

    @Transactional
    public void unregisterSession(WebSocketSession session) {
        meetingSessionRepository.delete(session);
    }

    @Transactional
    public void processAndSaveAudioData(WebSocketSession session, BinaryMessage message) throws IOException {
        long userId = meetingSessionRepository.getUserIdBySession(session)
                .orElseThrow(() -> new CustomException(AudioError.SESSION_INFO_NOT_FOUND));

        long meetingId = meetingSessionRepository.getMeetingIdBySession(session)
                .orElseThrow(() -> new CustomException(AudioError.SESSION_INFO_NOT_FOUND));

        long expectedChunkId = audioProgressRepository.findLastProcessedChunkId(userId, meetingId)
                .orElse(-1L) + 1;

        ByteBuffer buffer = message.getPayload();
        AudioMeta audioMetaResult = getAudioMeta(buffer, userId, meetingId);
        byte[] audioData = extractAudioData(buffer);

        if (expectedChunkId != audioMetaResult.chunkId()) {
            log.error("[session: {}]Expected chunkId {} does not match received chunkId {}.", session.getId(), expectedChunkId, audioMetaResult.chunkId());
        }

        audioRepository.save(audioMetaResult, audioData);
        audioProgressRepository.saveLastProcessedChunkId(userId, meetingId, audioMetaResult.chunkId());
        whisperClient.sendDiarized(meetingId, userId, audioData);
    }

    @EventListener
    public void handleMeetingCompletedEvent(MeetingCompletedEvent event) {
        Long meetingId = event.getMeetingId();
        log.info("Received MeetingCompletedEvent for meetingId: {}. Starting audio processing.", meetingId);

        try {
            List<Long> userIds = audioRepository.getAllUserIdByMeetingId(meetingId);

            if (userIds.isEmpty()) {
                log.info("No participants with audio found for meetingId: {}. Skipping audio processing.", meetingId);
                return;
            }

            for (Long userId : userIds) {
                processUserAudio(meetingId, userId);
            }
        } catch (IOException e) {
            log.error("Error processing audio directories or files for meetingId: {}", meetingId, e);
        }
    }

    private void processUserAudio(Long meetingId, Long userId) {
        try {
            List<Path> chunkPaths = audioRepository.getAllAudioChunkPaths(meetingId, userId);
            if (chunkPaths.isEmpty()) {
                log.info("No audio chunks found for meetingId: {}, userId: {} after re-checking. Skipping.", meetingId, userId);
                return;
            }
            mergeAndUploadUserAudio(meetingId, userId, chunkPaths);
        } catch (IOException e) {
            log.error("Error getting audio chunk paths for meetingId: {}, userId: {}", meetingId, userId, e);
        }
    }

    private void registerSessionAndValidateAccess(WebSocketSession session, Long meetingId, Long userId) {
        meetingSessionRepository.findByUserId(userId).ifPresent(webSocketSession -> closeAndRemoveExistingSession(webSocketSession, userId));
        meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);
        meetingSessionRepository.save(session, userId, meetingId);
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

    private void mergeAndUploadUserAudio(Long meetingId, Long userId, List<Path> chunkPaths) throws IOException {
        sortAudioChunks(chunkPaths);

        Path mergedAudioFile = mergeChunksToTempFile(meetingId, userId, chunkPaths);
        String s3Url = uploadAudioToS3(meetingId, userId, mergedAudioFile);
        updateMeetingUserWithRecordUrl(meetingId, userId, s3Url);
        deleteTemporaryFile(mergedAudioFile);
    }

    private void sortAudioChunks(List<Path> chunkPaths) {
        chunkPaths.sort((p1, p2) -> {
            try {
                String fileName1 = p1.getFileName().toString();
                String fileName2 = p2.getFileName().toString();
                long chunkId1 = Long.parseLong(fileName1);
                long chunkId2 = Long.parseLong(fileName2);
                return Long.compare(chunkId1, chunkId2);
            } catch (NumberFormatException | NullPointerException e) {
                log.warn("Could not parse chunkId from filename for sorting. p1: {}, p2: {}", p1.getFileName(), p2.getFileName(), e);
                return 0;
            }
        });
    }

    private Path mergeChunksToTempFile(Long meetingId, Long userId, List<Path> chunkPaths) throws IOException {
        Path tempFile = Files.createTempFile("merged_audio_" + meetingId + "_" + userId, ".opus");
        try (OutputStream os = Files.newOutputStream(tempFile)) {
            for (Path chunkFile : chunkPaths) {
                if (Files.exists(chunkFile) && Files.isReadable(chunkFile)) {
                    Files.copy(chunkFile, os);
                } else {
                    log.warn("Audio chunk file not found or not readable, skipping: {}", chunkFile);
                }
            }
        } catch (IOException e) {
            deleteTemporaryFile(tempFile);
            throw e;
        }
        return tempFile;
    }

    private String uploadAudioToS3(Long meetingId, Long userId, Path audioFilePath) throws IOException {
        String fileName = audioFilePath.getFileName().toString();
        String s3Key = "audio-records/meeting-" + meetingId + "/user-" + userId + "/" + fileName;
        return s3Client.uploadFile(s3Key, audioFilePath);
    }

    private void updateMeetingUserWithRecordUrl(Long meetingId, Long userId, String s3Url) {
        try {
            MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, userId)
                    .orElseThrow(() -> new CustomException(AudioError.MEETING_USER_NOT_FOUND));
            meetingUser.updateRecordUrl(s3Url);
            meetingUserRepository.save(meetingUser);
        } catch (CustomException e) {
            log.error("Error updating meeting user record url for meetingId: {}, userId: {}, s3 URL: {}", meetingId, userId, s3Url, e);
        }
    }

    private void deleteTemporaryFile(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Failed to delete temporary file: {}", filePath, e);
        }
    }
} 
