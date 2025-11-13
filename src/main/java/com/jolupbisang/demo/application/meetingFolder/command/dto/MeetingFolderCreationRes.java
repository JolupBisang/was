package com.jolupbisang.demo.application.meetingFolder.command.dto;

public record MeetingFolderCreationRes(
        Long folderId
) {
    public static MeetingFolderCreationRes of(Long folderId) {
        return new MeetingFolderCreationRes(folderId);
    }
}


