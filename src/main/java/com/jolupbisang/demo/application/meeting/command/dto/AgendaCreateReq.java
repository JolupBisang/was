package com.jolupbisang.demo.application.meeting.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "회의 안건 생성 요청")
public record AgendaCreateReq(
        @Schema(description = "안건 제목", example = "프로젝트 진행 상황", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "안건 내용 목록은 필수입니다.")
        List<@NotBlank(message = "안건 내용은 필수입니다.") String> contents
) {
} 
