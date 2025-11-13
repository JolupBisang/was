package com.jolupbisang.demo.application.meetingFolder.command.dto;

import java.util.List;

public record MeetingFolderUpdateRes(
        List<Long> successMeetingIds
) {
    public static MeetingFolderUpdateRes of(List<Long> successMeetingIds) {
        return new MeetingFolderUpdateRes(successMeetingIds);
    }
}

