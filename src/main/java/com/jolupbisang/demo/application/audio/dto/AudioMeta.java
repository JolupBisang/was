package com.jolupbisang.demo.application.audio.dto;

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
