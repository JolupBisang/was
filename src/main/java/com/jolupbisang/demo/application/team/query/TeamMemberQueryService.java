package com.jolupbisang.demo.application.team.query;

import com.jolupbisang.demo.application.team.query.dto.TeamMemberRes;
import com.jolupbisang.demo.domain.team.model.Team;
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
public class TeamMemberQueryService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public TeamMemberRes getTeamMembers(Long teamId, Long accessUserId) {
        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new NotFoundException("teamId: %d", teamId));

        team.validateViewAuthority(accessUserId);

        List<Long> memberIds = team.getMembers().stream()
                .map(member -> member.getUserId())
                .toList();

        List<User> members = userRepository.findAllById(memberIds);

        return TeamMemberRes.from(members);
    }
}

