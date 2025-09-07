package com.jolupbisang.demo.domain.audio.model;

import com.jolupbisang.demo.domain.audio.exception.AudioDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import lombok.Getter;

import java.time.LocalDateTime;

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
            throw new DomainException(AudioDomainErrorCode.UNSUPPORTED_ENCODING_TYPE, "encodingType: %s", encodingType.getType());
        }
        this.encodingType = encodingType;
    }

    private void setUserId(long userId) {
        if (userId < 0) {
            throw new DomainException(AudioDomainErrorCode.NEGATIVE_USER_ID, "userId: %d", userId);
        }
        this.userId = userId;
    }

    private void setCreatedDateTime(LocalDateTime createdDateTime) {
        if (createdDateTime == null || createdDateTime.isAfter(LocalDateTime.now())) {
            throw new DomainException(AudioDomainErrorCode.FUTURE_CREATED_DATE_TIME, "createdDateTime: %s", createdDateTime);
        }
        this.createdDateTime = createdDateTime;
    }

    private void setAudio(byte[] audio) {
        if (audio == null || audio.length == 0) {
            throw new DomainException(AudioDomainErrorCode.EMPTY_AUDIO_DATA);
        }
        this.audio = audio;
    }
}
