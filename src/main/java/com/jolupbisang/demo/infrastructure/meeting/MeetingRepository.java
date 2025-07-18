package com.jolupbisang.demo.infrastructure.meeting;

import com.jolupbisang.demo.domain.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingRepositoryCustom {
}
