package com.jolupbisang.demo.presentation.audio.parser;

import java.time.LocalDateTime;

public record AudioChunkDto(
        String type,
        long chunkId,
        String encoding,
        LocalDateTime timestamp,
        byte[] audioData
) {
}
