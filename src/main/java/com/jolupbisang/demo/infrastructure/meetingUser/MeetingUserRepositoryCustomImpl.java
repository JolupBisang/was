package com.jolupbisang.demo.infrastructure.meetingUser;

import com.jolupbisang.demo.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jolupbisang.demo.domain.meetingUser.QMeetingUser.*;
import static com.jolupbisang.demo.domain.user.QUser.*;

@RequiredArgsConstructor
public class MeetingUserRepositoryCustomImpl implements MeetingUserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> findParticipantsByMeetingId(Long meetingId) {
        return jpaQueryFactory.select(user)
            .from(meetingUser)
            .innerJoin(meetingUser.user, user)
            .where(meetingUser.meeting.id.eq(meetingId))
            .fetch();
    }
}
