package com.jolupbisang.demo.application.team.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TeamCreationReq(
        @NotBlank(message = "팀 이름은 1글자 이상이어야 합니다.")
        String teamName,

        @NotNull(message = "멤버 목록은 필수 입니다.")
        List<@Email(message = "멤버 이메일은 이메일 형식이어야 합니다.") String> memberEmails
) {
}
