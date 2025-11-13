package com.jolupbisang.demo.application.meetingFolder.command.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MeetingFolderDeletionReq(
        @NotEmpty(message = "폴더 ID 목록은 필수입니다.")
        @NotNull(message = "폴더 ID 목록은 필수입니다.")
        List<Long> folderIds
) {
}

