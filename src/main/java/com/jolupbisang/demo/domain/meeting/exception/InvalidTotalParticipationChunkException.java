package com.jolupbisang.demo.domain.meeting.exception;

import java.util.Map;

import com.jolupbisang.demo.global.exception.DomainException;

public class InvalidTotalParticipationChunkException extends DomainException {

    public InvalidTotalParticipationChunkException() {
        super(MeetingDomainErrorCode.INVALID_TOTAL_PARTICIPATION_CHUNK);
    }

    public InvalidTotalParticipationChunkException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.INVALID_TOTAL_PARTICIPATION_CHUNK, values);
    }

}
