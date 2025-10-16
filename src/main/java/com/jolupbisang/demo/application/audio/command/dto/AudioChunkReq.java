package com.jolupbisang.demo.application.audio.command.dto;

import java.time.LocalDateTime;

public record AudioChunkReq(
        String type,
        long chunkId,
        String encoding,
        LocalDateTime timestamp,
        byte[] audioData
) {
}
