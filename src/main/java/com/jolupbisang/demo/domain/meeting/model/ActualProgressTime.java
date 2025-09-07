package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.MeetingDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor
public class ActualProgressTime {
    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    public ActualProgressTime(LocalDateTime actualStartTime, LocalDateTime actualEndTime) {
        validateTimeOrder(actualStartTime, actualEndTime);
        this.actualStartTime = actualStartTime;
        this.actualEndTime = actualEndTime;
    }

    private void validateTimeOrder(LocalDateTime actualStartTime, LocalDateTime actualEndTime) {
        if (actualStartTime == null && actualEndTime != null) {
            throw new DomainException(MeetingDomainErrorCode.EMPTY_ACTUAL_START_TIME);
        }
        if (actualStartTime != null && actualStartTime.isAfter(actualEndTime)) {
            throw new DomainException(MeetingDomainErrorCode.END_TIME_BEFORE_START_TIME, "actualStartTime: %s, actualEndTime: %s", actualStartTime, actualEndTime);
        }
    }
}
