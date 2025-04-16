package com.jolupbisang.demo.presentation.meeting.dto.response;

public record MeetingCreationRes(
        Long meetingId
) {

    public static MeetingCreationRes of(Long meetingId) {
        return new MeetingCreationRes(meetingId);
    }
}
