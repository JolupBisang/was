package com.jolupbisang.demo.application.audio.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AudioErrorCode implements ErrorCode {
    SESSION_INFO_NOT_FOUND(HttpStatus.UNAUTHORIZED, "세션 정보를 찾을 수 없거나 유효하지 않습니다. 다시 연결해주세요."),
    INVALID_MEETING_ID_FORMAT(HttpStatus.BAD_REQUEST, "미팅 ID 형식이 올바르지 않습니다."),

    INVALID_META_DATA(HttpStatus.BAD_REQUEST, "잘못된 메타 데이터 형식입니다."),
    METADATA_TYPE_INVALID(HttpStatus.BAD_REQUEST, "오디오 메타데이터의 'type' 필드가 null이거나 비어있습니다."),
    METADATA_CHUNKID_NULL(HttpStatus.BAD_REQUEST, "오디오 메타데이터의 'chunkId' 필드가 null입니다."),
    METADATA_ENCODING_INVALID(HttpStatus.BAD_REQUEST, "오디오 메타데이터의 'encoding' 필드가 null이거나 비어있습니다."),
    METADATA_TIMESTAMP_NULL(HttpStatus.BAD_REQUEST, "오디오 메타데이터의 'timestamp' 필드가 null이거나 유효한 날짜/시간 형식이 아닙니다."),
    METADATA_INVALID_PAYLOAD_LENGTH(HttpStatus.BAD_REQUEST, "오디오 메타데이터 페이로드 길이가 유효하지 않습니다."),
    MEETING_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회의의 사용자를 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}
