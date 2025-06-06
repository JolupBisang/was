package com.jolupbisang.demo.presentation.audio.dto.response;

import java.time.LocalDateTime;

public record ConnectionEstablishedRes(
        long lastProcessedChunkId,
        LocalDateTime meetingStartTime
) {

    public static ConnectionEstablishedRes of(long lastProcessedChunkId, LocalDateTime meetingStartTime) {
        return new ConnectionEstablishedRes(lastProcessedChunkId, meetingStartTime);
    }
}
