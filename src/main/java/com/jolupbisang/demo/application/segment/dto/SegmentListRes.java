package com.jolupbisang.demo.application.segment.dto;

import com.jolupbisang.demo.domain.segment.Segment;

import java.time.LocalDateTime;

public record SegmentListRes(
        Long id,
        Long userId,
        String userName,
        int segmentOrder,
        LocalDateTime timestamp,
        String text,
        String lang
) {

    public static SegmentListRes from(Segment segment) {
        return new SegmentListRes(
                segment.getId(),
                segment.getUser().getId(),
                segment.getUser().getNickname(),
                segment.getSegmentOrder(),
                segment.getTimestamp(),
                segment.getText(),
                segment.getLang()
        );
    }
} 
