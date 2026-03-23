package com.jolupbisang.demo.application.meetingFolder.command.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MeetingFolderUpdateReq(
        @NotEmpty(message = "회의 ID 목록은 필수입니다.")
        @NotNull(message = "회의 ID 목록은 필수입니다.")
        List<Long> meetingIds
) {
}
