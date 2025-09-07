package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.MeetingDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ScheduledTime {

    @Column(name = "scheduled_start_time")
    private LocalDateTime scheduledStartTime;

    @Column(name = "scheduled_end_time")
    private LocalDateTime scheduledEndTime;

    public ScheduledTime(LocalDateTime scheduledStartTime, LocalDateTime scheduledEndTime) {
        setScheduledStartTime(scheduledStartTime);
        setScheduledEndTime(scheduledEndTime);
        validateTimeOrder(scheduledStartTime, scheduledEndTime);
    }

    public ScheduledTime(LocalDateTime scheduledStartTime, int targetMinutes) {
        this(scheduledStartTime, scheduledStartTime.plusMinutes(targetMinutes));
    }

    public int getTargetTime() {
        return (int) scheduledEndTime.toLocalTime().toSecondOfDay() - (int) scheduledStartTime.toLocalTime().toSecondOfDay();
    }

    private void setScheduledStartTime(LocalDateTime scheduledStartTime) {
        if (scheduledStartTime == null) {
            throw new DomainException(MeetingDomainErrorCode.EMPTY_START_TIME);
        }
        this.scheduledStartTime = scheduledStartTime;
    }

    private void setScheduledEndTime(LocalDateTime scheduledEndTime) {
        if (scheduledEndTime == null) {
            throw new DomainException(MeetingDomainErrorCode.EMPTY_END_TIME);
        }
        this.scheduledEndTime = scheduledEndTime;
    }


    private void validateTimeOrder(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new DomainException(MeetingDomainErrorCode.END_TIME_BEFORE_START_TIME, "startTime: %s, endTime: %s", startTime, endTime);
        }
    }
}
