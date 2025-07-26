package com.jolupbisang.demo.infrastructure.meeting;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.ParticipantStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.jolupbisang.demo.domain.meeting.entity.QMeeting.meeting;
import static com.jolupbisang.demo.domain.meeting.entity.QMeetingUser.meetingUser;


@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Meeting> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth) {
        return queryFactory.selectFrom(meeting)
                .where(meeting.id.in(
                                JPAExpressions.select(meetingUser.meeting.id)
                                        .from(meetingUser)
                                        .where(meetingUser.user.id.eq(userId)
                                                .and(meetingUser.status.eq(ParticipantStatus.ACCEPTED))))
                        .and(meeting.scheduledTime.scheduledStartTime.between(startOfMonth, endOfMonth)))
                .fetch();
    }
}
