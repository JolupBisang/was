package com.jolupbisang.demo.application.meeting.command.dto;

import jakarta.validation.constraints.NotNull;

public record TeamTagAdditionReq(
        @NotNull(message = "팀 ID는 필수입니다.")
        Long teamId
) {
}

