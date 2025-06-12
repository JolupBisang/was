package com.jolupbisang.demo.application.segment.dto;

import com.jolupbisang.demo.infrastructure.audio.client.dto.response.DiarizedResponse;

public record SegmentMessage(
        long meetingId,
        DiarizedResponse.Segment segmentData
) {

    public static SegmentMessage of(DiarizedResponse.Segment segmentData, long meetingId) {
        return new SegmentMessage(meetingId, segmentData);
    }
}
