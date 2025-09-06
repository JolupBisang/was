package com.jolupbisang.demo.application.audio.exception;

import com.jolupbisang.demo.global.exception.BusinessException;

import java.util.Map;

public class UnaccessibleAudioFileException extends BusinessException {
    public UnaccessibleAudioFileException(Map<String, Object> values, Throwable cause) {
        super(AudioApplicationErrorCode.UNACCESSIBLE_AUDIO_FILE, values, cause);
    }

    public UnaccessibleAudioFileException(Throwable cause) {
        super(AudioApplicationErrorCode.UNACCESSIBLE_AUDIO_FILE, cause);
    }

    public UnaccessibleAudioFileException() {
        super(AudioApplicationErrorCode.UNACCESSIBLE_AUDIO_FILE);
    }
}
