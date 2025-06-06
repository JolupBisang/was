package com.jolupbisang.demo.infrastructure.participationRate;

import com.jolupbisang.demo.domain.participationRate.ParticipationRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRateRepository extends JpaRepository<ParticipationRate, Long> {

    List<ParticipationRate> findByMeetingId(Long meetingId);
}
