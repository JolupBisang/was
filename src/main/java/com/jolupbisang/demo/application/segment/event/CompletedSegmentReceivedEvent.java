package com.jolupbisang.demo.application.segment.event;

import java.util.List;

public record CompletedSegmentReceivedEvent(
        long meetingId,
        List<SegmentDto> completed
) {
}
