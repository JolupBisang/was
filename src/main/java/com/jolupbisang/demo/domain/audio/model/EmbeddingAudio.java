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
        if (createdDateTime == null) {
            throw new DomainException(AudioDomainErrorCode.NULL_CREATED_DATE_TIME);
        }
        
        // 클라이언트-서버 간 시계 동기화 차이와 네트워크 지연을 고려하여 ±30초 허용
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minAllowedTime = now.minusSeconds(30);
        LocalDateTime maxAllowedTime = now.plusSeconds(30);
        
        if (createdDateTime.isBefore(minAllowedTime) || createdDateTime.isAfter(maxAllowedTime)) {
            throw new DomainException(AudioDomainErrorCode.FUTURE_CREATED_DATE_TIME, 
                "timestamp out of acceptable range. createdDateTime: %s, now: %s", createdDateTime, now);
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
