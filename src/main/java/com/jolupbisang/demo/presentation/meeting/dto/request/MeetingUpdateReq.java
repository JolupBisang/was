package com.jolupbisang.demo.presentation.meeting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class MeetingUpdateReq {

    @NotBlank(message = "회의 제목은 필수입니다.")
    @Size(min = 1, max = 100, message = "회의 제목은 1자 이상 100자 이하이어야 합니다.")
    @Schema(description = "회의 제목")
    private String title;

    @NotBlank(message = "회의 장소는 필수입니다.")
    @Size(min = 1, max = 100, message = "회의 장소는 1자 이상 100자 이하이어야 합니다.")
    @Schema(description = "회의 장소")
    private String location;

    @NotNull(message = "회의 시작 시간은 필수입니다.")
    @Future(message = "회의 시작 시간은 현재 시간 이후여야 합니다.")
    @Schema(description = "회의 예정 시작 시간")
    private LocalDateTime scheduledStartTime;

    @NotNull(message = "회의 목표 시간은 필수입니다.")
    @Positive(message = "회의 목표 시간은 양수여야 합니다.")
    @Schema(description = "회의 목표 시간 (분 단위)")
    private Integer targetTime;

    @NotNull(message = "회의 휴식 시간은 필수입니다.")
    @Positive(message = "회의 휴식 시간은 양수여야 합니다.")
    @Schema(description = "회의 휴식 시간 (분 단위)")
    private Integer restInterval;

    @NotEmpty(message = "참여자 목록은 필수입니다.")
    @Schema(description = "회의 참여자 이메일 목록")
    private List<@Email(message = "참여자는 이메일 형식이어야 합니다.") String> participants;

    @NotEmpty(message = "회의 안건 목록은 필수입니다.")
    @Schema(description = "회의 안건 목록")
    private List<@Size(min = 1, message = "회의 안건은 1글자 이상이어야 합니다.") String> agendas;
} 
