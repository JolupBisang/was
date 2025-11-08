package com.jolupbisang.demo.application.feedback.event;

import java.time.LocalDateTime;

public record FeedbackReceivedEvent(
        long meetingId,
        long userId,
        String comment,
        LocalDateTime generatedDateTime
) {
}
