package com.jolupbisang.demo.domain.audio.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptyAudioDataException extends DomainException {
    public EmptyAudioDataException(Map<String, Object> values) {
        super(AudioDomainErrorCode.EMPTY_AUDIO_DATA, values);
    }

    public EmptyAudioDataException() {
        super(AudioDomainErrorCode.EMPTY_AUDIO_DATA);
    }
}
