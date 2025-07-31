package com.jolupbisang.demo.infrastructure.whisper.event;

import java.time.LocalDateTime;

public record FeedbackReceivedEvent(
        long meetingId,
        long userId,
        String comment,
        LocalDateTime generatedDateTime
) {
}
