package com.jolupbisang.demo.application.team.command;

import com.jolupbisang.demo.application.team.command.dto.TeamDeletionRes;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamDeletionService {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamDeletionRes delete(Long teamId, Long userId) {
        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new NotFoundException("팀을 찾을 수 없습니다. teamId: %d", teamId));

        team.validateOwnerAuthority(userId);
        teamRepository.delete(team);

        return new TeamDeletionRes(teamId);
    }
}

