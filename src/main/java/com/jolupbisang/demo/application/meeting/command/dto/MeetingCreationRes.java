package com.jolupbisang.demo.application.meeting.command.dto;

public record MeetingCreationRes(
        long meetingId
) {

    public static MeetingCreationRes of(long meetingId) {
        return new MeetingCreationRes(meetingId);
    }
}
