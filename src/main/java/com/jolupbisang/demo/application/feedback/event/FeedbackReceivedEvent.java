package com.jolupbisang.demo.application.feedback.event;

import java.time.LocalDateTime;
import java.util.List;

public record FeedbackReceivedEvent(
        long meetingId,
        long userId,
        String comment,
        List<Long> ids,
        LocalDateTime generatedDateTime
) {
}
