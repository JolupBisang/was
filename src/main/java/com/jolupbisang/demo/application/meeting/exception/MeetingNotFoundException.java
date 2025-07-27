package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.application.common.NotFoundException;

import java.util.List;

public class MeetingNotFoundException extends NotFoundException {
    public MeetingNotFoundException(List<Object> values) {
        super(MeetingApplicationErrorCode.NOT_FOUND_MEETING, values);
    }

    public MeetingNotFoundException() {
        super(MeetingApplicationErrorCode.NOT_FOUND_MEETING);
    }
}
