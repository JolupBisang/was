package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.global.exception.BusinessException;

import java.util.List;

public class NotParticipantException extends BusinessException {

    public NotParticipantException(List<Object> values) {
        super(MeetingApplicationErrorCode.NOT_PARTICIPANT, values);
    }

    public NotParticipantException() {
        super(MeetingApplicationErrorCode.NOT_PARTICIPANT);
    }
}
