package com.jolupbisang.demo.presentation.meeting.dto.response;

import com.jolupbisang.demo.domain.meeting.Meeting;

import java.time.LocalDateTime;

public record MeetingDetailRes(
        Long meetingId,
        String title,
        String location,
        LocalDateTime scheduledStartTime,
        Integer targetTime,
        Integer restInterval,
        Integer restDuration,
        String meetingStatus
) {

    public static MeetingDetailRes fromEntity(Meeting meeting) {
        return new MeetingDetailRes(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getLocation(),
                meeting.getScheduledStartTime(),
                meeting.getTargetTime(),
                meeting.getRestInterval(),
                meeting.getRestDuration(),
                meeting.getMeetingStatus().name()
        );
    }
}
