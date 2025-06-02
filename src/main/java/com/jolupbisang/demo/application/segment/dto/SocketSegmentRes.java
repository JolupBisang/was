package com.jolupbisang.demo.application.segment.dto;

import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.DiarizedResponse;
import java.time.LocalDateTime;

public record SocketSegmentRes(
    LocalDateTime timestamp,
    long userId,
    int order,
    String text
) {
    public static SocketSegmentRes of(DiarizedResponse.Segment segmentData, LocalDateTime timestamp) {
        return new SocketSegmentRes(
                timestamp,
                segmentData.userId(),
                segmentData.order(),
                segmentData.text()
        );
    }
}
