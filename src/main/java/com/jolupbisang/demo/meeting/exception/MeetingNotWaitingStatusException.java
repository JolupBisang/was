package com.jolupbisang.demo.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;
import com.jolupbisang.demo.global.exception.ErrorCode;

import java.util.List;

public class MeetingNotWaitingStatusException extends DomainException {
    public MeetingNotWaitingStatusException(ErrorCode errorCode, List<Object> values) {
        super(errorCode, values);
    }

    public MeetingNotWaitingStatusException() {
        super(MeetingDomainErrorCode.NOT_WAITING_STATUS);
    }
}
