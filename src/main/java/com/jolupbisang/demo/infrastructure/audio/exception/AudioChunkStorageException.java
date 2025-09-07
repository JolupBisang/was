package com.jolupbisang.demo.infrastructure.audio.exception;

import com.jolupbisang.demo.global.exception.InfraException;

import java.util.Map;

public class AudioChunkStorageException extends InfraException {
    public AudioChunkStorageException(Map<String, Object> values, Throwable cause) {
        super(AudioChunkInfraErrorCode.AUDIO_CHUNK_STORAGE_FAILED, values, cause);
    }

    public AudioChunkStorageException(Throwable cause) {
        super(AudioChunkInfraErrorCode.AUDIO_CHUNK_STORAGE_FAILED, cause);
    }

    public AudioChunkStorageException() {
        super(AudioChunkInfraErrorCode.AUDIO_CHUNK_STORAGE_FAILED);
    }
}
