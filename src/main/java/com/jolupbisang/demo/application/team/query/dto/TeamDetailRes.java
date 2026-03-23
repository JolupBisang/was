package com.jolupbisang.demo.application.team.query.dto;

import com.jolupbisang.demo.domain.team.model.Team;

public record TeamDetailRes(
        Long teamId,
        String teamName
) {
    public static TeamDetailRes fromEntity(Team team) {
        return new TeamDetailRes(
                team.getId(),
                team.getTeamName().getName()
        );
    }
}

