package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;
import com.jolupbisang.demo.global.exception.ErrorCode;

import java.util.Map;

public class MeetingNotWaitingStatusException extends DomainException {
    public MeetingNotWaitingStatusException(ErrorCode errorCode, Map<String, Object> values) {
        super(errorCode, values);
    }

    public MeetingNotWaitingStatusException() {
        super(MeetingDomainErrorCode.NOT_WAITING_STATUS);
    }
}
