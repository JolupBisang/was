package com.jolupbisang.demo.infrastructure.segment;

import com.jolupbisang.demo.domain.segment.model.QSegment;
import com.jolupbisang.demo.domain.segment.model.Segment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
}
