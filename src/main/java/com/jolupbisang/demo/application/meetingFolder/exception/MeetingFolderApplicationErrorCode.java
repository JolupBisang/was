package com.jolupbisang.demo.application.meetingFolder.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MeetingFolderApplicationErrorCode implements ErrorCode {
    NOT_FOUND_FOLDER("MFA-0001", HttpStatus.NOT_FOUND, "존재하지 않는 폴더입니다."),
    NOT_FOUND_MEETING("MFA-0002", HttpStatus.NOT_FOUND, "존재하지 않는 회의입니다."),
    NOT_FOLDER_OWNER("MFA-0003", HttpStatus.FORBIDDEN, "폴더 소유자만 접근할 수 있습니다."),
    NOT_MEETING_PARTICIPANT("MFA-0004", HttpStatus.FORBIDDEN, "회의 참여자만 폴더에 추가할 수 있습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}

