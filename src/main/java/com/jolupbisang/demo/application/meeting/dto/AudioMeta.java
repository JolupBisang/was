package com.jolupbisang.demo.application.meeting.dto;

import java.time.LocalDateTime;

public record AudioMeta(
        String type,
        long userId,
        long meetingId,
        long chunkId,
        LocalDateTime timestamp,
        String encoding
) {
}
