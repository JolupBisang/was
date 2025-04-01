package com.jolupbisang.demo.application.meeting.dto;

import java.time.LocalDateTime;

public record AudioMeta(
        String type,
        long userId,
        long meetingId,
        int chunkId,
        LocalDateTime timestamp,
        String encoding
) {
}
