package com.jolupbisang.demo.application.summary.event;

import java.time.LocalDateTime;

public record SummaryReceivedEvent(
        long meetingId,
        String content,
        boolean isRecap,
        LocalDateTime generatedDateTime
) {
}
