package com.jolupbisang.demo.infrastructure.meetingFolder;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.jolupbisang.demo.domain.meeting.model.QMeeting.meeting;
import static com.jolupbisang.demo.domain.meetingFolder.model.QMeetingFolder.meetingFolder;
import static com.jolupbisang.demo.domain.meetingFolder.model.QMeetingFolderMeeting.meetingFolderMeeting;

@RequiredArgsConstructor
public class MeetingFolderRepositoryImpl implements MeetingFolderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Meeting> findClosestMeetingByMeetingIds(List<Long> meetingIds, LocalDateTime now) {
        if (meetingIds == null || meetingIds.isEmpty()) {
            return Optional.empty();
        }

        // 1. 미래 회의 중 가장 가까운 회의 조회
        Meeting futureMeeting = queryFactory
                .selectFrom(meeting)
                .where(meeting.id.in(meetingIds)
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
                .where(meeting.id.in(meetingIds)
                        .and(meeting.scheduledTime.scheduledStartTime.loe(now)))
                .orderBy(meeting.scheduledTime.scheduledStartTime.desc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(pastMeeting);
    }

    @Override
    public Optional<MeetingFolder> findByIdWithMeetings(Long folderId) {
        MeetingFolder folder = queryFactory
                .selectFrom(meetingFolder)
                .leftJoin(meetingFolder.meetings, meetingFolderMeeting).fetchJoin()
                .where(meetingFolder.id.eq(folderId))
                .fetchOne();

        return Optional.ofNullable(folder);
    }
}


