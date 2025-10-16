package com.jolupbisang.demo.infrastructure.meeting;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.ParticipantStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.jolupbisang.demo.domain.meeting.model.QAgenda.agenda;
import static com.jolupbisang.demo.domain.meeting.model.QMeeting.meeting;
import static com.jolupbisang.demo.domain.meeting.model.QParticipant.participant;


@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Meeting> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth) {
        return queryFactory
                .selectFrom(meeting)
                .where(meeting.id.in(findAcceptedMeetingByUserId(userId))
                        .and(meeting.scheduledTime.scheduledStartTime.between(startOfMonth, endOfMonth)))
                .fetch();
    }

    private static JPQLQuery<Long> findAcceptedMeetingByUserId(Long userId) {
        return JPAExpressions.select(participant.meeting.id)
                .from(participant)
                .where(participant.userId.eq(userId)
                        .and(participant.status.eq(ParticipantStatus.ACCEPTED)));
    }

    @Override
    public Optional<Meeting> findByIdWithParticipant(long meetingId) {
        Meeting resultMeeting = queryFactory
                .selectFrom(meeting)
                .leftJoin(meeting.participants, participant).fetchJoin()
                .where(meeting.id.eq(meetingId))
                .fetchOne();

        return Optional.ofNullable(resultMeeting);
    }

    @Override
    public Optional<Meeting> findByIdWithAgenda(long meetingId, long agendaId) {
        Meeting resultMeeting = queryFactory.selectFrom(meeting)
                .leftJoin(meeting.agendas, agenda).fetchJoin()
                .where(meeting.id.eq(meetingId))
                .fetchOne();

        return Optional.ofNullable(resultMeeting);
    }

    @Override
    public Optional<Meeting> findByIdWithAllDetail(long meetingId) {
        return Optional.ofNullable(queryFactory.selectFrom(meeting)
                .leftJoin(meeting.participants, participant).fetchJoin()
                .leftJoin(meeting.agendas, agenda).fetchJoin()
                .where(meeting.id.eq(meetingId))
                .fetchOne());
    }

}
