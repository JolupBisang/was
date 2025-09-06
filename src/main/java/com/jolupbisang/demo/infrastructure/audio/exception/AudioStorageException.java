package com.jolupbisang.demo.infrastructure.audio.exception;

import com.jolupbisang.demo.global.exception.InfraException;

import java.util.Map;

public class AudioStorageException extends InfraException {
    public AudioStorageException(Map<String, Object> values, Throwable cause) {
        super(AudioInfraErrorCode.AUDIO_STORAGE_FAILED, values, cause);
    }

    public AudioStorageException(Throwable cause) {
        super(AudioInfraErrorCode.AUDIO_STORAGE_FAILED, cause);
    }

    public AudioStorageException() {
        super(AudioInfraErrorCode.AUDIO_STORAGE_FAILED);
    }
}
