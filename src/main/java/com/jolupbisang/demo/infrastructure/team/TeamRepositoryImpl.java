package com.jolupbisang.demo.infrastructure.team;

import com.jolupbisang.demo.domain.team.model.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
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

    @Override
    public Slice<Team> findByNameContainingAndUserId(String name, Long userId, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        List<Team> teams = queryFactory
                .selectFrom(team)
                .innerJoin(team.members, teamMember)
                .where(
                        teamMember.userId.eq(userId)
                                .and(team.teamName.name.containsIgnoreCase(name))
                )
                .distinct()
                .orderBy(team.id.desc())
                .limit(pageSize + 1)
                .offset(pageable.getOffset())
                .fetch();

        boolean hasNext = teams.size() > pageSize;
        if (hasNext) {
            teams.remove(teams.size() - 1);
        }

        return new SliceImpl<>(teams, pageable, hasNext);
    }
}

