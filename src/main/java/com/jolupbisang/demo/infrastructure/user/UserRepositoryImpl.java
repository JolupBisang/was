package com.jolupbisang.demo.infrastructure.user;

import com.jolupbisang.demo.domain.user.User;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jolupbisang.demo.domain.meetingUser.QMeetingUser.meetingUser;
import static com.jolupbisang.demo.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findByMeetingId(Long meetingId) {
        return queryFactory.selectFrom(user)
                .where(user.id.in(
                        JPAExpressions.select(meetingUser.user.id)
                                .from(meetingUser)
                                .where(meetingUser.meeting.id.eq(meetingId))
                ))
                .fetch();
    }
} 
