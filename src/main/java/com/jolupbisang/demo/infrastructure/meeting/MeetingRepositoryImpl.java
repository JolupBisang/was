package com.jolupbisang.demo.infrastructure.meeting;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.MeetingStatus;
import com.jolupbisang.demo.domain.meeting.model.ParticipantStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.jolupbisang.demo.domain.meeting.model.QAgenda.agenda;
import static com.jolupbisang.demo.domain.meeting.model.QMeeting.meeting;
import static com.jolupbisang.demo.domain.meeting.model.QParticipant.participant;
import static com.jolupbisang.demo.domain.meeting.model.QTeamTag.teamTag;


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
        // 첫 번째 쿼리: participants를 fetch join
        Meeting resultMeeting = queryFactory.selectFrom(meeting)
                .leftJoin(meeting.participants, participant).fetchJoin()
                .where(meeting.id.eq(meetingId))
                .fetchOne();

        if (resultMeeting != null) {
            queryFactory.selectFrom(meeting)
                    .leftJoin(meeting.agendas, agenda).fetchJoin()
                    .where(meeting.id.eq(meetingId))
                    .fetchOne();
            
            queryFactory.selectFrom(meeting)
                    .leftJoin(meeting.teamTags, teamTag).fetchJoin()
                    .where(meeting.id.eq(meetingId))
                    .fetchOne();
        }

        return Optional.ofNullable(resultMeeting);
    }

    @Override
    public Optional<Meeting> findByIdWithTeamTagsAndParticipants(long meetingId) {
        Meeting resultMeeting = queryFactory
                .selectFrom(meeting)
                .leftJoin(meeting.participants, participant).fetchJoin()
                .where(meeting.id.eq(meetingId))
                .fetchOne();

        if (resultMeeting != null) {
            queryFactory.selectFrom(meeting)
                    .leftJoin(meeting.teamTags, teamTag).fetchJoin()
                    .where(meeting.id.eq(meetingId))
                    .fetchOne();
        }

        return Optional.ofNullable(resultMeeting);
    }

    @Override
    public List<Meeting> findByScheduledTimeAndParticipantAndStatuses(LocalDateTime startTime, LocalDateTime endTime, long userId, Set<MeetingStatus> statuses) {

        return queryFactory.selectFrom(meeting)
                .innerJoin(meeting.participants, participant)
                .where(participant.userId.eq(userId)
                        .and(meeting.scheduledTime.scheduledStartTime.lt(endTime))
                        .and(meeting.scheduledTime.scheduledEndTime.gt(startTime))
                        .and(meeting.meetingStatus.in(statuses)))
                .fetch();
    }

    @Override
    public Optional<Meeting> findClosestMeetingByTeamId(Long teamId, LocalDateTime now) {
        // 1. 미래 회의 중 가장 가까운 회의 조회
        Meeting futureMeeting = queryFactory
                .selectFrom(meeting)
                .innerJoin(meeting.teamTags, teamTag)
                .where(teamTag.teamId.eq(teamId)
                        .and(meeting.scheduledTime.scheduledStartTime.gt(now)))
                .orderBy(meeting.scheduledTime.scheduledStartTime.asc())
                .limit(1)
                .fetchOne();

        if (futureMeeting != null) {
            return Optional.of(futureMeeting);
        }

        // 2. 지난 회의 중 가장 최근 회의 조회
        Meeting pastMeeting = queryFactory
                .selectFrom(meeting)
                .innerJoin(meeting.teamTags, teamTag)
                .where(teamTag.teamId.eq(teamId)
                        .and(meeting.scheduledTime.scheduledStartTime.loe(now)))
                .orderBy(meeting.scheduledTime.scheduledStartTime.desc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(pastMeeting);
    }
}

