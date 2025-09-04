package com.jolupbisang.demo.application.segment.query.dto;

import com.jolupbisang.demo.domain.segment.model.Segment;

import java.time.LocalDateTime;

public record SegmentDetailRes(
        long id,
        long userId,
        int segmentOrder,
        LocalDateTime timestamp,
        String text,
        String lang
) {

    public static SegmentDetailRes from(Segment segment) {
        return new SegmentDetailRes(
                segment.getId(),
                segment.getUserId(),
                segment.getOrder(),
                segment.getSpokenDateTime(),
                segment.getText(),
                segment.getLang()
        );
    }
}
