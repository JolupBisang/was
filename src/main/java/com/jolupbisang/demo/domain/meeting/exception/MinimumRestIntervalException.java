package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.BusinessException;

import java.util.List;

public class MinimumRestIntervalException extends BusinessException {

    public MinimumRestIntervalException(List<Object> values) {
        super(MeetingDomainErrorCode.MINIMUM_REST_INTERVAL, values);
    }
}
