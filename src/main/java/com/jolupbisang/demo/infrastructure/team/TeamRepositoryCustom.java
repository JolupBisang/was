package com.jolupbisang.demo.infrastructure.team;

import com.jolupbisang.demo.domain.team.model.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface TeamRepositoryCustom {
    Optional<Team> findByIdWithMembers(Long teamId);
    Slice<Team> findByNameContainingAndUserId(String name, Long userId, Pageable pageable);
}

