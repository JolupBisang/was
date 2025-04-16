package com.jolupbisang.demo.presentation.agenda.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "회의 안건 상태 변경 요청")
public record AgendaStatusReq(
        @Schema(description = "완료 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "완료 상태는 null일 수 없습니다.")
        Boolean isCompleted
) {
}
