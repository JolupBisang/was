package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.ActualProgressTimeOrderException;
import com.jolupbisang.demo.domain.meeting.exception.EmptyActualStartTimeException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
            throw new EmptyActualStartTimeException(List.of(actualEndTime));
        }
        if (actualStartTime != null && actualStartTime.isAfter(actualEndTime)) {
            throw new ActualProgressTimeOrderException(Map.of("actualStartTime", actualStartTime, "actualEndTime", actualEndTime));
        }
    }
}
