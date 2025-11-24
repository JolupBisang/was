package com.jolupbisang.demo.infrastructure.segment;

import com.jolupbisang.demo.domain.segment.model.QSegment;
import com.jolupbisang.demo.domain.segment.model.Segment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SegmentRepositoryImpl implements SegmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Segment> findByMeetingIdAndSegmentOrders(long meetingId, List<Integer> orders) {
        return queryFactory.selectFrom(QSegment.segment)
                .where(QSegment.segment.meetingId.eq(meetingId)
                        .and(QSegment.segment.order.in(orders)))
                .fetch();
    }

    @Override
    public Map<Long, List<Segment>> findByMeetingIdsAndSegmentOrders(Map<Long, List<Integer>> meetingIdToOrders) {
        if (meetingIdToOrders.isEmpty()) {
            return Map.of();
        }

        // 모든 meetingId와 orders를 포함하는 쿼리
        List<Long> meetingIds = meetingIdToOrders.keySet().stream().toList();
        List<Integer> allOrders = meetingIdToOrders.values().stream()
                .flatMap(List::stream)
                .distinct()
                .toList();

        if (allOrders.isEmpty()) {
            return Map.of();
        }

        List<Segment> segments = queryFactory.selectFrom(QSegment.segment)
                .where(QSegment.segment.meetingId.in(meetingIds)
                        .and(QSegment.segment.order.in(allOrders)))
                .fetch();

        // meetingId별로 그룹화
        return segments.stream()
                .collect(Collectors.groupingBy(Segment::getMeetingId));
    }
}
