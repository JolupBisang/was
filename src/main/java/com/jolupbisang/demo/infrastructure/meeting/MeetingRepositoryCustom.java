package com.jolupbisang.demo.infrastructure.meeting;

import com.jolupbisang.demo.domain.meeting.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepositoryCustom {
    List<Meeting> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
