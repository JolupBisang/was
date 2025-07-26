package com.jolupbisang.demo.application.meeting.dto;

import com.jolupbisang.demo.domain.meeting.model.Meeting;

public record MeetingDetailSummary(
        Long id,
        String title,
        String location,
        String scheduledStartTime,
        int targetTime,
        String status
) {
    public static MeetingDetailSummary fromEntity(Meeting meeting) {
        return new MeetingDetailSummary(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getLocation(),
                meeting.getScheduledTime().getScheduledStartTime().toString(),
                meeting.getScheduledTime().getTargetTime(),
                meeting.getMeetingStatus().name()
        );
    }
}
