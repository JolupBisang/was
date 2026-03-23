package com.jolupbisang.demo.domain.audio.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AudioDomainErrorCode implements ErrorCode {
    NEGATIVE_USER_ID("AD-0001", HttpStatus.BAD_REQUEST, "사용자 ID는 0보다 크거나 같아야 합니다."),
    UNSUPPORTED_ENCODING_TYPE("AD-0002", HttpStatus.BAD_REQUEST, "지원되지 않는 오디오 인코딩 타입입니다."),
    NULL_CREATED_DATE_TIME("AD-0003", HttpStatus.BAD_REQUEST, "생성 시간은 필수입니다."),
    FUTURE_CREATED_DATE_TIME("AD-0004", HttpStatus.BAD_REQUEST, "생성 시간은 필수이며 현재 시간보다 이전이어야합니다."),
    NULL_AUDIO_DATA("AD-0005", HttpStatus.BAD_REQUEST, "오디오 데이터는 필수입니다."),
    EMPTY_AUDIO_DATA("AD-0006", HttpStatus.BAD_REQUEST, "오디오 데이터가 비어있습니다."),
    NEGATIVE_MEETING_ID("AD-0007", HttpStatus.BAD_REQUEST, "청크 ID는 0보다 크거나 같아야 합니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
