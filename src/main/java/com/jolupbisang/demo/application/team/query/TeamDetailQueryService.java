package com.jolupbisang.demo.application.team.query;

import com.jolupbisang.demo.application.team.query.dto.TeamDetailRes;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamDetailQueryService {

    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public TeamDetailRes getTeamDetail(Long teamId, Long accessUserId) {
        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new NotFoundException("teamId: %d", teamId));

        team.validateViewAuthority(accessUserId);

        return TeamDetailRes.fromEntity(team);
    }
}

