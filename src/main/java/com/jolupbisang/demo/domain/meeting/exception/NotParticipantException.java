package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.application.meeting.exception.MeetingApplicationErrorCode;
import com.jolupbisang.demo.global.exception.ApplicationException;

import java.util.Map;

public class NotParticipantException extends ApplicationException {

    public NotParticipantException(Map<String, Object> values) {
        super(MeetingApplicationErrorCode.NOT_PARTICIPANT, values);
    }

    public NotParticipantException() {
        super(MeetingApplicationErrorCode.NOT_PARTICIPANT);
    }
}
