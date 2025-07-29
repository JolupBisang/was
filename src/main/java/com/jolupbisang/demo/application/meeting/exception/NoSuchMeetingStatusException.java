package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.global.exception.BusinessException;

import java.util.Map;

public class NoSuchMeetingStatusException extends BusinessException {

    public NoSuchMeetingStatusException(Map<String, Object> values) {
        super(MeetingApplicationErrorCode.NO_SUCH_MEETING_STATUS, values);
    }

    public NoSuchMeetingStatusException() {
        super(MeetingApplicationErrorCode.NO_SUCH_MEETING_STATUS);
    }
}
