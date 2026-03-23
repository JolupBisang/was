package com.jolupbisang.demo.application.meetingFolder.command.dto;

import jakarta.validation.constraints.NotBlank;

public record MeetingFolderCreationReq(
        @NotBlank(message = "폴더 이름은 필수입니다.")
        String name
) {
}


