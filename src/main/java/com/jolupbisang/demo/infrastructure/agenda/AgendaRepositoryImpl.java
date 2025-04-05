package com.jolupbisang.demo.infrastructure.agenda;

import com.jolupbisang.demo.domain.agenda.Agenda;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.jolupbisang.demo.domain.agenda.QAgenda.agenda;
import static com.jolupbisang.demo.domain.meetingUser.QMeetingUser.meetingUser;

@RequiredArgsConstructor
public class AgendaRepositoryImpl implements AgendaRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Agenda> findByAgendaIdAndUserId(Long agendaId, Long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(agenda)
                        .where(agenda.meeting.id.in(
                                        JPAExpressions
                                                .select(meetingUser.meeting.id)
                                                .from(meetingUser)
                                                .where(meetingUser.user.id.eq(userId))
                                )
                                .and(agenda.id.eq(agendaId)))
                        .fetchOne()
        );
    }
}
