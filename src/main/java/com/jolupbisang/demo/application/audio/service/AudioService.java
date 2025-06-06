package com.jolupbisang.demo.application.audio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.audio.dto.AudioMeta;
import com.jolupbisang.demo.application.audio.dto.StepFunctionOutput;
import com.jolupbisang.demo.application.audio.exception.AudioErrorCode;
import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.common.MeetingSessionManager;
import com.jolupbisang.demo.application.event.MeetingCompletedEvent;
import com.jolupbisang.demo.application.event.MeetingStartingEvent;
import com.jolupbisang.demo.application.event.whisper.WhisperEmbeddedEvent;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.audio.EmbeddedVectorRepository;
import com.jolupbisang.demo.infrastructure.audio.EmbeddingAudioRepository;
import com.jolupbisang.demo.infrastructure.aws.sfn.SfnClientUtil;
import com.jolupbisang.demo.infrastructure.meeting.audio.AudioProgressRepository;
import com.jolupbisang.demo.infrastructure.meeting.audio.AudioRepository;
import com.jolupbisang.demo.infrastructure.meeting.client.WhisperClient;
import com.jolupbisang.demo.infrastructure.meetingUser.MeetingUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AudioService {

    private final AudioRepository audioRepository;
    private final AudioProgressRepository audioProgressRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final EmbeddingAudioRepository embeddingAudioRepository;
    private final EmbeddedVectorRepository embeddedVectorRepository;

    private final MeetingAccessValidator meetingAccessValidator;
    private final MeetingSessionManager meetingSessionManager;
    private final WhisperClient whisperClient;
    private final SfnClientUtil sfnClientUtil;

    private final ObjectMapper objectMapper;

    @Value("${cloud.aws.sfn.merge-audio-state-machine-arn}")
    private String MERGE_AUDIO_STATE_MACHINE_ARN;

    @Transactional
    public long processSessionStartAndGetLastProcessedChunkId(WebSocketSession session, Long userId, Long meetingId) {
        registerSessionAndValidateAccess(session, meetingId, userId);

        return audioProgressRepository.findLastProcessedChunkId(userId, meetingId)
                .orElse(-1L);
    }

    public void unregisterSession(WebSocketSession session) {
        meetingSessionManager.delete(session);
    }

    public void processAndSaveAudioData(WebSocketSession session, BinaryMessage message) throws IOException {
        long userId = meetingSessionManager.getUserIdBySession(session)
                .orElseThrow(() -> new CustomException(AudioErrorCode.SESSION_INFO_NOT_FOUND));

        long meetingId = meetingSessionManager.getMeetingIdBySession(session)
                .orElseThrow(() -> new CustomException(AudioErrorCode.SESSION_INFO_NOT_FOUND));

        long expectedChunkId = audioProgressRepository.findLastProcessedChunkId(userId, meetingId)
                .orElse(-1L) + 1;

        ByteBuffer buffer = message.getPayload();
        AudioMeta audioMetaResult = getAudioMeta(buffer, userId, meetingId);
        byte[] audioData = extractAudioData(buffer);

        if (expectedChunkId != audioMetaResult.chunkId()) {
            log.error("[session: {}]Expected chunkId {} does not match received chunkId {}.", session.getId(), expectedChunkId, audioMetaResult.chunkId());
        }

        audioRepository.save(audioMetaResult, audioData);
        audioProgressRepository.saveLastProcessedChunkId(userId, meetingId, audioMetaResult.chunkId(), audioMetaResult.timestamp());
        whisperClient.sendDiarization(meetingId, userId, null, audioData);
    }

    @Transactional
    public void embeddingAudio(Long userId, MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            throw new CustomException(AudioErrorCode.INVALID_EMBEDDING_AUDIO);
        }

        if (!"audio/mp4".equals(audioFile.getContentType())) {
            throw new CustomException(AudioErrorCode.INVALID_EMBEDDING_AUDIO_TYPE);
        }

        try {
            embeddingAudioRepository.save(userId, audioFile.getBytes());
            whisperClient.sendEmbeddingAudio(userId, audioFile.getBytes());
        } catch (IOException e) {
            throw new CustomException(AudioErrorCode.INVALID_EMBEDDING_AUDIO);
        }
    }

    @EventListener
    public void handleMeetingStartingEvent(MeetingStartingEvent event) {
        List<Long> users = meetingUserRepository.findUserIdByMeetingId(event.getMeetingId());

        List<byte[]> totalVectors = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (Long userId : users) {
            List<byte[]> userVectors = embeddedVectorRepository.findAllByUserId(userId);
            counts.add(userVectors.size());
            totalVectors.addAll(userVectors);
        }

        whisperClient.sendRefenceVector(event.getMeetingId(), users, counts, totalVectors);
    }

    @EventListener
    public void handleMeetingCompletedEvent(MeetingCompletedEvent event) {
        long meetingId = event.getMeetingId();
        StepFunctionOutput stepFunctionOutput = sfnClientUtil.startMergeAudioStateMachine(MERGE_AUDIO_STATE_MACHINE_ARN, meetingId);

        if (stepFunctionOutput != null) {
            if (stepFunctionOutput.statusCode().equals("400")) {
                log.error("[StepFunction] meetingId:{}, statusCode: {}", event.getMeetingId(), stepFunctionOutput.statusCode());
            }
        } else {
            log.error("[StepFunction] return null");
        }
    }

    @EventListener
    public void handleWhisperEmbeddedEvent(WhisperEmbeddedEvent event) {
        embeddedVectorRepository.save(event.getUserId(), event.getAudio());
    }

    private void registerSessionAndValidateAccess(WebSocketSession session, Long meetingId, Long userId) {
        meetingSessionManager.findByUserId(userId).ifPresent(webSocketSession -> closeAndRemoveExistingSession(webSocketSession, userId));
        meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);
        meetingSessionManager.save(session, userId, meetingId);
    }

    private AudioMeta getAudioMeta(ByteBuffer byteBuffer, long userId, long meetingId) {
        AudioDetails audioDetails = extractAudioDetails(byteBuffer);
        return audioDetails.toAudioMeta(userId, meetingId);
    }

    private AudioDetails extractAudioDetails(ByteBuffer byteBuffer) {
        int metaLength = byteBuffer.getInt();
        if (metaLength <= 0 || metaLength > byteBuffer.remaining()) {
            throw new CustomException(AudioErrorCode.METADATA_INVALID_PAYLOAD_LENGTH);
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
            throw new CustomException(AudioErrorCode.INVALID_META_DATA);
        }
    }

    private byte[] extractAudioData(ByteBuffer byteBuffer) {
        byte[] audioBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(audioBytes);
        return audioBytes;
    }

    private void closeAndRemoveExistingSession(WebSocketSession existingSession, Long userId) {
        log.warn("User {} already has an active session {}. Closing the old session.", userId, existingSession.getId());
        try {
            existingSession.close(CloseStatus.POLICY_VIOLATION.withReason("New connection established"));
        } catch (IOException e) {
            log.error("Error closing existing WebSocket session {} for user {}: {}", existingSession.getId(), userId, e.getMessage());
        }
        meetingSessionManager.deleteByUserId(userId);
    }

    private record AudioDetails(String type, Long chunkId, String encoding, LocalDateTime timestamp) {
        public AudioDetails {
            if (type == null || type.trim().isEmpty()) {
                throw new CustomException(AudioErrorCode.METADATA_TYPE_INVALID);
            }
            if (chunkId == null) {
                throw new CustomException(AudioErrorCode.METADATA_CHUNKID_NULL);
            }
            if (encoding == null || encoding.trim().isEmpty()) {
                throw new CustomException(AudioErrorCode.METADATA_ENCODING_INVALID);
            }
            if (timestamp == null) {
                throw new CustomException(AudioErrorCode.METADATA_TIMESTAMP_NULL);
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
} 
