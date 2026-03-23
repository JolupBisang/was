package com.jolupbisang.demo.infrastructure.audio.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AudioInfraErrorCode implements ErrorCode {
    AUDIO_STORAGE_FAILED("AI-0001", HttpStatus.INTERNAL_SERVER_ERROR, "오디오 저장에 실패했습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
