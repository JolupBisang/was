package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NotHostException extends DomainException {
    public NotHostException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.ONLY_FOR_HOST_AUTHORITY, values);
    }

    public NotHostException() {
        super(MeetingDomainErrorCode.ONLY_FOR_HOST_AUTHORITY);
    }
}
