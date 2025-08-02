package com.jolupbisang.demo.infrastructure.meeting;

import com.jolupbisang.demo.domain.meeting.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeetingRepositoryCustom {
    List<Meeting> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);

    Optional<Meeting> findByIdWithParticipant(long meetingId);

    Optional<Meeting> findByIdWithAgenda(long meetingId, long agendaId);

    Optional<Meeting> findByIdWithAllDetail(long meetingId);
}
