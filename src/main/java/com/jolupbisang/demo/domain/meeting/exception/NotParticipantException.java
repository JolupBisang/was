package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.application.meeting.exception.MeetingApplicationErrorCode;
import com.jolupbisang.demo.global.exception.BusinessException;

import java.util.Map;

public class NotParticipantException extends BusinessException {

    public NotParticipantException(Map<String, Object> values) {
        super(MeetingApplicationErrorCode.NOT_PARTICIPANT, values);
    }

    public NotParticipantException() {
        super(MeetingApplicationErrorCode.NOT_PARTICIPANT);
    }
}
