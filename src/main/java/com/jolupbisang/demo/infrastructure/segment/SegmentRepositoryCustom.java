package com.jolupbisang.demo.infrastructure.segment;

import com.jolupbisang.demo.domain.segment.model.Segment;

import java.util.List;
import java.util.Map;

public interface SegmentRepositoryCustom {

    List<Segment> findByMeetingIdAndSegmentOrders(long meetingId, List<Integer> orders);

    Map<Long, List<Segment>> findByMeetingIdsAndSegmentOrders(Map<Long, List<Integer>> meetingIdToOrders);
}
