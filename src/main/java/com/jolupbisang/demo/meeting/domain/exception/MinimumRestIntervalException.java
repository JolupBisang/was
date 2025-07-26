package com.jolupbisang.demo.meeting.domain.exception;

import com.jolupbisang.demo.global.exception.BusinessException;

import java.util.List;

public class MinimumRestIntervalException extends BusinessException {

    public MinimumRestIntervalException(List<Object> values) {
        super(MeetingDomainErrorCode.MINIMUM_REST_INTERVAL, values);
    }
}
