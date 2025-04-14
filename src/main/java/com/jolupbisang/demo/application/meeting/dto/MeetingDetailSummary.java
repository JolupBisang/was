package com.jolupbisang.demo.application.meeting.dto;

import com.jolupbisang.demo.domain.meeting.Meeting;

public record MeetingDetailSummary(
        Long id,
        String title,
        String location,
        String scheduledStartTime,
        String status
) {
    public static MeetingDetailSummary fromEntity(Meeting meeting) {
        return new MeetingDetailSummary(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getLocation(),
                meeting.getScheduledStartTime().toString(),
                meeting.getMeetingStatus().name());
    }
}
