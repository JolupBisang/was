package com.jolupbisang.demo.domain.meeting;

import com.jolupbisang.demo.domain.meeting.exception.EmptyEndTimeException;
import com.jolupbisang.demo.domain.meeting.exception.EndTimeBeforeStartTimeException;
import com.jolupbisang.demo.domain.meeting.exception.StartTimeNullException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public int getTargetTime() {
        return (int) scheduledEndTime.toLocalTime().toSecondOfDay() - (int) scheduledStartTime.toLocalTime().toSecondOfDay();
    }

    private void setScheduledStartTime(LocalDateTime scheduledStartTime) {
        if (scheduledStartTime == null) {
            throw new StartTimeNullException();
        }
        this.scheduledStartTime = scheduledStartTime;
    }

    private void setScheduledEndTime(LocalDateTime scheduledEndTime) {
        if (scheduledEndTime == null) {
            throw new EmptyEndTimeException(List.of(scheduledEndTime));
        }
        this.scheduledEndTime = scheduledEndTime;
    }


    private void validateTimeOrder(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new EndTimeBeforeStartTimeException(List.of(startTime, endTime));
        }
    }
}
