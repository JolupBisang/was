package com.jolupbisang.demo.presentation.agenda.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회의 안건 수정 요청")
public record AgendaUpdateReq(
        @Schema(description = "수정할 안건 내용", example = "수정된 프로젝트 진행 상황 논의", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "안건 내용은 필수입니다.")
        String content
) {
} 