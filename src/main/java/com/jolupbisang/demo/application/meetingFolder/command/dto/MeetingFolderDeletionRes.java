package com.jolupbisang.demo.application.meetingFolder.command.dto;

import java.util.List;

public record MeetingFolderDeletionRes(
        List<Long> successFolderIds
) {
    public static MeetingFolderDeletionRes of(List<Long> successFolderIds) {
        return new MeetingFolderDeletionRes(successFolderIds);
    }
}

