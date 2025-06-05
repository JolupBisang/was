package com.jolupbisang.demo.presentation.meetingUser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;

import java.util.List;

@Schema(description = "회의 참여자 추가 요청")
public record ParticipantAddReq(
        @Schema(description = "참여자 이메일 목록", example = "[\"participant1@example.com\", \"participant2@example.com\"]", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "참여자 이메일 목록은 필수입니다.")
        List<@Email(message = "올바른 이메일 형식이어야 합니다.") String> emails
) {
} 