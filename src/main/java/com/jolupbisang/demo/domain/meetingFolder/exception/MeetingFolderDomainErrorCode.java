package com.jolupbisang.demo.domain.meetingFolder.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MeetingFolderDomainErrorCode implements ErrorCode {
    EMPTY_FOLDER_NAME("MFD-0001", HttpStatus.BAD_REQUEST, "폴더 이름은 필수입니다."),
    MEETING_ALREADY_IN_FOLDER("MFD-0002", HttpStatus.BAD_REQUEST, "이미 폴더에 포함된 회의입니다."),
    MEETING_NOT_IN_FOLDER("MFD-0003", HttpStatus.BAD_REQUEST, "폴더에 포함되지 않은 회의입니다."),
    INVALID_USER_ID("MFD-0004", HttpStatus.BAD_REQUEST, "사용자 ID는 0보다 커야합니다."),
    NON_EXISTING_MEETING("MFD-0005", HttpStatus.BAD_REQUEST, "존재하지 않는 회의입니다."),
    NOT_FOLDER_OWNER("MFD-0006", HttpStatus.FORBIDDEN, "폴더 소유자만 접근할 수 있습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}


