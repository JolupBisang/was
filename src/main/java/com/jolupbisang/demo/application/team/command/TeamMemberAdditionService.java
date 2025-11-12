package com.jolupbisang.demo.application.team.command;

import com.jolupbisang.demo.application.team.command.dto.TeamMemberAdditionReq;
import com.jolupbisang.demo.application.team.command.dto.TeamMemberAdditionRes;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.domain.team.model.TeamMemberRole;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamMemberAdditionService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamMemberAdditionRes addMember(Long teamId, Long accessUserId, TeamMemberAdditionReq request) {
        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new NotFoundException("teamId: %d", teamId));

        team.validateOwnerAuthority(accessUserId);

        User userToAdd = userRepository.findByEmail(request.memberEmail())
                .orElseThrow(() -> new NotFoundException("이메일로 사용자를 찾을 수 없습니다. email: %s", request.memberEmail()));

        team.addMember(userToAdd.getId(), TeamMemberRole.TEAM_MEMBER);

        return new TeamMemberAdditionRes(team.getId());
    }
}

