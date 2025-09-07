package com.jolupbisang.demo.domain.audio.model;

import com.jolupbisang.demo.domain.audio.exception.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class AudioChunk {

    private long meetingId;
    private long userId;
    private long chunkId;
    private AudioEncodingType encodingType;
    private LocalDateTime createdDateTime;
    private byte[] audioData;

    public AudioChunk(long meetingId, long userId, long chunkId, AudioEncodingType encodingType, LocalDateTime createdDateTime, byte[] audioData) {
        setUserId(userId);
        setMeetingId(meetingId);
        setChunkId(chunkId);
        setEncodingType(encodingType);
        setCreatedDateTime(createdDateTime);
        setAudioData(audioData);
    }

    private void setUserId(long userId) {
        if (userId < 0) {
            throw new InvalidUserIdException(Map.of("userId", userId));
        }
        this.userId = userId;
    }

    private void setMeetingId(long meetingId) {
        if (meetingId < 0) {
            throw new IllegalArgumentException("Meeting ID must be positive");
        }
        this.meetingId = meetingId;
    }

    private void setChunkId(long chunkId) {
        if (chunkId < 0) {
            throw new IllegalArgumentException("Chunk ID must be positive");
        }
        this.chunkId = chunkId;
    }

    private void setEncodingType(AudioEncodingType encodingType) {
        if (!AudioEncodingType.AUDIO_PCM.equals(encodingType)) {
            throw new UnsupportedEncodingTypeException(Map.of("encodingType", encodingType));
        }
        this.encodingType = encodingType;
    }

    private void setCreatedDateTime(LocalDateTime createdDateTime) {
        if (createdDateTime == null) {
            throw new NullCreatedDateTimeException();
        }
        if (createdDateTime.isAfter(LocalDateTime.now())) {
            throw new FutureCreatedDateTimeException(Map.of("createdDateTime", createdDateTime));
        }
        this.createdDateTime = createdDateTime;
    }

    private void setAudioData(byte[] audioData) {
        if (audioData == null) {
            throw new NullAudioDataException();
        }
        if (audioData.length == 0) {
            throw new EmptyAudioDataException();
        }
        this.audioData = audioData;
    }
}
