package com.jolupbisang.demo.application.team.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TeamMemberAdditionReq(
        @NotBlank(message = "멤버 이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        String memberEmail
) {
}

