package com.jolupbisang.demo.domain.audio.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullAudioDataException extends DomainException {
    public NullAudioDataException(Map<String, Object> values) {
        super(AudioDomainErrorCode.NULL_AUDIO_DATA, values);
    }

    public NullAudioDataException() {
        super(AudioDomainErrorCode.NULL_AUDIO_DATA);
    }
}
