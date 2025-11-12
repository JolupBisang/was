package com.jolupbisang.demo.infrastructure.team;

import com.jolupbisang.demo.domain.team.model.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.jolupbisang.demo.domain.team.model.QTeam.team;
import static com.jolupbisang.demo.domain.team.model.QTeamMember.teamMember;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Team> findByIdWithMembers(Long teamId) {
        Team resultTeam = queryFactory
                .selectFrom(team)
                .leftJoin(team.members, teamMember).fetchJoin()
                .where(team.id.eq(teamId))
                .fetchOne();

        return Optional.ofNullable(resultTeam);
    }
}

