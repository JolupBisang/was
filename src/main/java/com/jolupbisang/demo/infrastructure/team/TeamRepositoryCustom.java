package com.jolupbisang.demo.infrastructure.team;

import com.jolupbisang.demo.domain.team.model.Team;

import java.util.Optional;

public interface TeamRepositoryCustom {
    Optional<Team> findByIdWithMembers(Long teamId);
}

