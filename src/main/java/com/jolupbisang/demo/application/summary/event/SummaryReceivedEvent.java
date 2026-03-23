package com.jolupbisang.demo.application.summary.event;

import java.time.LocalDateTime;
import java.util.List;

public record SummaryReceivedEvent(
        long meetingId,
        String content,
        List<Long> ids,
        boolean isRecap,
        LocalDateTime generatedDateTime
) {
}
