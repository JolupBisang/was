package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.global.exception.BusinessException;

import java.util.List;

public class InvalidDateException extends BusinessException {
    public InvalidDateException(List<Object> values) {
        super(MeetingApplicationErrorCode.INVALID_QUERY_DATE, values);
    }

    public InvalidDateException() {
        super(MeetingApplicationErrorCode.INVALID_QUERY_DATE);
    }
}
