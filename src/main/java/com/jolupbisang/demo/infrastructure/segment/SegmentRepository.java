package com.jolupbisang.demo.infrastructure.segment;

import com.jolupbisang.demo.domain.segment.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SegmentRepository extends JpaRepository<Segment, Long> {
    Optional<Segment> findByMeetingIdAndSegmentOrder(Long meetingId, int segmentOrder);
} 