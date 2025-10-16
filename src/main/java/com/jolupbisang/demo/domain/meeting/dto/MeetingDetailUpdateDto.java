package com.jolupbisang.demo.domain.meeting.dto;

import com.jolupbisang.demo.domain.meeting.model.RestTime;
import com.jolupbisang.demo.domain.meeting.model.ScheduledTime;

public record MeetingDetailUpdateDto(
        String title,
        String location,
        ScheduledTime scheduledTime,
        RestTime RestTime
) {
}
