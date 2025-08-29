package com.jolupbisang.demo.application.segment.event;

import java.util.List;

public record CandidateSegmentReceivedEvent(
        long meetingId,
        List<SegmentDto> candidate
) {
}
