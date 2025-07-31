package com.jolupbisang.demo.domain.feedback.event;

import java.time.LocalDateTime;

public record FeedbackCreatedEvent(
        long meetingId,
        long userId,
        String comment,
        LocalDateTime generatedDateTime
) {
}
