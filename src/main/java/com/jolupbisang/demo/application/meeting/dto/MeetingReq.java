package com.jolupbisang.demo.application.meeting.dto;

import com.jolupbisang.demo.domain.meeting.Meeting;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingReq(
        @NotBlank(message = "회의 제목은 필수입니다.")
        String title,

        @NotBlank(message = "회의 장소는 필수입니다.")
        String location,

        @NotNull(message = "회의 시작 시간은 필수입니다.")
        LocalDateTime scheduledStartTime,

        @NotNull(message = "회의 목표 시간은 필수입니다.")
        @Min(value = 1, message = "회의 목표 시간은 1분 이상이어야 합니다.")
        Integer targetTime,

        @NotNull(message = "회의 휴식 시간은 필수입니다.")
        @Min(value = 0, message = "회의 휴식 시간은 0분 이상이어야 합니다.")
        Integer restInterval,

        @NotNull(message = "참여자 목록은 필수입니다.")
        List<@Email(message = "참여자는 이메일 형식이어야 합니다.") String> participants,

        @NotNull(message = "회의 안건 목록은 필수입니다.")
        List<@NotBlank(message = "회의 안건은 1글자 이상이어야 합니다.") String> agendas
) {
    public Meeting toEntity() {
        return new Meeting(title, location, scheduledStartTime, targetTime, restInterval);
    }
}
