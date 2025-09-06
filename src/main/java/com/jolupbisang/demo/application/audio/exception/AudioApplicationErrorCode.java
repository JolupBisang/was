package com.jolupbisang.demo.application.audio.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AudioApplicationErrorCode implements ErrorCode {
    UNACCESSIBLE_AUDIO_FILE("AA-0001", HttpStatus.BAD_REQUEST, "오디오 파일에 접근할 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
