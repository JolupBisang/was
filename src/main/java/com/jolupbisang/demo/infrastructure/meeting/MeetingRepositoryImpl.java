package com.jolupbisang.demo.infrastructure.meeting;

import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.meetingUser.MeetingUserStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.jolupbisang.demo.domain.meeting.QMeeting.meeting;
import static com.jolupbisang.demo.domain.meetingUser.QMeetingUser.meetingUser;

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
                                                .and(meetingUser.status.eq(MeetingUserStatus.ACCEPTED))))
                        .and(meeting.scheduledStartTime.between(startOfMonth, endOfMonth)))
                .fetch();
    }
}
