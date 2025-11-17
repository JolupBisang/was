package com.jolupbisang.demo.infrastructure.meetingFolder;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MeetingFolderRepositoryCustom {
    Optional<Meeting> findClosestMeetingByMeetingIds(java.util.List<Long> meetingIds, LocalDateTime now);
    Optional<MeetingFolder> findByIdWithMeetings(Long folderId);
    Slice<MeetingFolder> findByNameContainingAndUserId(String name, Long userId, Pageable pageable);
}


