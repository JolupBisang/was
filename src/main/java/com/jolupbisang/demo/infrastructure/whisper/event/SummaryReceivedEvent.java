package com.jolupbisang.demo.infrastructure.whisper.event;

import java.time.LocalDateTime;

public record SummaryReceivedEvent(
        long meetingId,
        String content,
        boolean isRecap,
        LocalDateTime generatedDateTime
) {
}
