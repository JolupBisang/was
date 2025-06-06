package com.jolupbisang.demo.infrastructure.meeting.audio;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AudioProgressRepository {
    Optional<Long> findLastProcessedChunkId(Long userId, Long meetingId);

    void saveLastProcessedChunkId(Long userId, Long meetingId, Long chunkId, LocalDateTime timestamp);

    void deleteAudioProgress(Long userId, Long meetingId);

    Optional<LocalDateTime> findFirstProcessedTime(Long meetingId);
} 
