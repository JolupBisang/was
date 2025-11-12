package com.jolupbisang.demo.application.team.query;

import com.jolupbisang.demo.application.team.query.dto.TeamListRes;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamListQueryService {

    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public List<TeamListRes> getTeamsByUserId(Long userId) {
        List<Team> teams = teamRepository.findByMembersUserId(userId);
        
        return teams.stream()
                .map(TeamListRes::fromEntity)
                .toList();
    }
}

