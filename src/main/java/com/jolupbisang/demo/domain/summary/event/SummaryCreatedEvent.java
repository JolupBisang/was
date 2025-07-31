package com.jolupbisang.demo.domain.summary.event;

import java.time.LocalDateTime;

public record SummaryCreatedEvent(
        long meetingId,
        String content,
        LocalDateTime generatedDateTime
) {
}
