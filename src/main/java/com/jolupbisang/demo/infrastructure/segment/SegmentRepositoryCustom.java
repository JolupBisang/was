package com.jolupbisang.demo.infrastructure.segment;

import com.jolupbisang.demo.domain.segment.model.Segment;

import java.util.List;

public interface SegmentRepositoryCustom {

    List<Segment> findByMeetingIdAndSegmentOrders(long meetingId, List<Integer> orders);
}
