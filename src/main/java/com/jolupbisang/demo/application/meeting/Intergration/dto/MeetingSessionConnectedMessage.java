package com.jolupbisang.demo.application.meeting.Intergration.dto;

import java.time.LocalDateTime;

public record MeetingSessionConnectedMessage(
        LocalDateTime actualStartTime,
        long lastProcessedChunkId
) {

    public static MeetingSessionConnectedMessage of(LocalDateTime actualStartTime, long lastProcessedChunkId) {
        return new MeetingSessionConnectedMessage(actualStartTime, lastProcessedChunkId);
    }
}
