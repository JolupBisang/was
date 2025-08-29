package com.jolupbisang.demo.infrastructure.segment;

import com.jolupbisang.demo.domain.segment.model.Segment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentRepository extends JpaRepository<Segment, Long>, SegmentRepositoryCustom {
    Slice<Segment> findByMeetingId(Long meetingId, Pageable pageable);
}
