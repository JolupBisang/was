package com.jolupbisang.demo.presentation.meeting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MeetingUpdateReq(
        @Schema(description = "회의 제목", example = "팀A 회의1")
        @NotBlank(message = "회의 제목은 필수입니다.")
        String title,

        @Schema(description = "회의 장소", example = "회의실 1")
        @NotBlank(message = "회의 장소는 필수입니다.")
        String location,

        @Schema(description = "회의 시작 시간", example = "2023-10-01T10:00:00")
        @NotNull(message = "회의 시작 시간은 필수입니다.")
        LocalDateTime scheduledStartTime,

        @Schema(description = "회의 목표 시간(분)", example = "60")
        @NotNull(message = "회의 목표 시간은 필수입니다.")
        @Min(value = 1, message = "회의 목표 시간은 1분 이상이어야 합니다.")
        Integer targetTime,

        @Schema(description = "회의 휴식 시간(분)", example = "10")
        @NotNull(message = "회의 휴식 시간은 필수입니다.")
        @Min(value = 0, message = "회의 휴식 시간 간격은 0분 이상이어야 합니다.")
        Integer restInterval,

        @Schema(description = "회의 휴식 시간 길이(분)", example = "5")
        @NotNull(message = "회의 휴식 시간 길이는 필수입니다.")
        @Min(value = 1, message = "회의 휴식 시간 길이는 1분 이상이어야 합니다.")
        Integer restDuration
) {

}
