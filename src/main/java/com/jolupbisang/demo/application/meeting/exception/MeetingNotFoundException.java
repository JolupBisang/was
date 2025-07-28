package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.application.common.NotFoundException;

import java.util.Map;

public class MeetingNotFoundException extends NotFoundException {
    public MeetingNotFoundException(Map<String, Object> values) {
        super(MeetingApplicationErrorCode.NOT_FOUND_MEETING, values);
    }

    public MeetingNotFoundException() {
        super(MeetingApplicationErrorCode.NOT_FOUND_MEETING);
    }
}
