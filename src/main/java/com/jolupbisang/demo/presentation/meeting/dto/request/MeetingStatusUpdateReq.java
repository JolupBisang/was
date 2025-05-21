package com.jolupbisang.demo.presentation.meeting.dto.request;

import jakarta.validation.constraints.NotNull;

public record MeetingStatusUpdateReq(
        @NotNull(message = "변경할 회의 상태는 필수입니다.")
        String targetStatus
) {
} 
