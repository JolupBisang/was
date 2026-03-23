package com.jolupbisang.demo.application.team;

import com.jolupbisang.demo.application.team.command.dto.TeamCreationReq;
import com.jolupbisang.demo.application.team.command.dto.TeamCreationRes;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.domain.team.model.TeamName;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamCreationService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamCreationRes create(TeamCreationReq teamCreationReq, long creatorId) {
        if (!userRepository.existsById(creatorId)) {
            throw new NotFoundException("팀 생성자 정보가 없습니다. creatorId: %d", creatorId);
        }

        List<User> members = userRepository.findByEmailIn(teamCreationReq.memberEmails());

        List<Long> memberIds = members.stream()
                .map(User::getId)
                .toList();

        Team team = new Team(
                new TeamName(teamCreationReq.teamName()),
                creatorId,
                memberIds
        );

        Team savedTeam = teamRepository.save(team);

        return new TeamCreationRes(savedTeam.getId());
    }
}
