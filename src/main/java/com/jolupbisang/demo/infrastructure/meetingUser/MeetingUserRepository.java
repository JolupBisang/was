package com.jolupbisang.demo.infrastructure.meetingUser;

import com.jolupbisang.demo.domain.meetingUser.MeetingUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Long> {
}
