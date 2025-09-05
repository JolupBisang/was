package com.jolupbisang.demo.domain.audio.model;

import com.jolupbisang.demo.domain.audio.exception.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class EmbeddingAudio {

    private long userId;
    private AudioEncodingType encodingType;
    private LocalDateTime createdDateTime;
    private byte[] audio;

    public EmbeddingAudio(long userId, AudioEncodingType encodingType, LocalDateTime createdDateTime, byte[] audio) {
        setUserId(userId);
        setEncodingType(encodingType);
        setCreatedDateTime(createdDateTime);
        setAudio(audio);
    }

    private void setEncodingType(AudioEncodingType encodingType) {
        if (!encodingType.equals(AudioEncodingType.AUDIO_MP4)) {
            throw new UnsupportedEncodingTypeException(Map.of("encodingType", encodingType));
        }
        this.encodingType = encodingType;
    }

    private void setUserId(long userId) {
        if (userId < 0) {
            throw new InvalidUserIdException(Map.of("userId", userId));
        }
        this.userId = userId;
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

    private void setAudio(byte[] audio) {
        if (audio == null) {
            throw new NullAudioDataException();
        }
        if (audio.length == 0) {
            throw new EmptyAudioDataException();
        }
        this.audio = audio;
    }
}
