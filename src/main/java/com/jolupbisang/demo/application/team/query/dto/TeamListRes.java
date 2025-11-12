package com.jolupbisang.demo.application.team.query.dto;

import com.jolupbisang.demo.domain.team.model.Team;

public record TeamListRes(
        Long teamId,
        String teamName
) {
    public static TeamListRes fromEntity(Team team) {
        return new TeamListRes(
                team.getId(),
                team.getTeamName().getName()
        );
    }
}

