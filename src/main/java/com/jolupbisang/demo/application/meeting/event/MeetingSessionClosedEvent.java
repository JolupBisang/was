package com.jolupbisang.demo.application.meeting.event;

public record MeetingSessionClosedEvent(
        long meetingId,
        long userId
) {
}
