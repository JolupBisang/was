package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SegmentDomainErrorCode implements ErrorCode {
    NULL_START_CHUNK("SD-0001", HttpStatus.BAD_REQUEST, "Start chunk cannot be null."),
    NULL_END_CHUNK("SD-0002", HttpStatus.BAD_REQUEST, "End chunk cannot be null."),
    EMPTY_TEXT("SD-0003", HttpStatus.BAD_REQUEST, "Text cannot be empty."),
    INVALID_SEGMENT_CHUNK_ORDER("SD-0004", HttpStatus.BAD_REQUEST, "Start chunk must be less than or equal to end chunk."),
    INVALID_MEETING_ID("SD-0005", HttpStatus.BAD_REQUEST, "잘못된 범위의 meetingId 입니다."),
    INVALID_USER_ID("SD-0006", HttpStatus.BAD_REQUEST, "잘못된 범위의 userId 입니다"),
    INVALID_ORDER("SD-0007", HttpStatus.BAD_REQUEST, "잘못된 범위의 order 값입니다."),
    EMPTY_SEGMENT_TEXT("SD-0009", HttpStatus.BAD_REQUEST, "Segment text cannot be empty."),
    EMPTY_SEGMENT_LANG("SD-0010", HttpStatus.BAD_REQUEST, "Segment language cannot be empty."),
    NULL_SPOKEN_DATE_TIME("SD-0011", HttpStatus.BAD_REQUEST, "Spoken date time cannot be null.");

    private final String code;
    private final HttpStatus status;
    private final String message;
} 
