package com.jolupbisang.demo.infrastructure.meetingUser;

import com.jolupbisang.demo.domain.meetingUser.MeetingUser;
import com.jolupbisang.demo.domain.meetingUser.MeetingUserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Long> {
    boolean existsByMeetingIdAndUserIdAndStatusIn(Long meetingId, Long userId, MeetingUserStatus... statuses);

    boolean existsByMeetingIdAndUserIdAndIsHost(Long meetingId, Long userId, boolean isLeader);

    Optional<MeetingUser> findByMeetingIdAndUserId(Long meetingId, Long userId);
}
