package com.jolupbisang.demo.infrastructure.meeting;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.MeetingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeetingRepositoryCustom {
    List<Meeting> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);

    Optional<Meeting> findByIdWithParticipant(long meetingId);

    Optional<Meeting> findByIdWithAgenda(long meetingId, long agendaId);

    Optional<Meeting> findByIdWithAllDetail(long meetingId);

    Optional<Meeting> findByIdWithTeamTagsAndParticipants(long meetingId);

    List<Meeting> findByScheduledTimeAndParticipantAndStatuses(LocalDateTime startTime, LocalDateTime endTime, long userId, Set<MeetingStatus> statuses);

    Optional<Meeting> findClosestMeetingByTeamId(Long teamId, LocalDateTime now);

    List<Meeting> findByIdsWithParticipant(List<Long> meetingIds);

    Slice<Meeting> findByTitleContainingAndUserId(String title, Long userId, Pageable pageable);
}
