package com.jolupbisang.demo.infrastructure.meeting.session;

import java.util.Optional;

public interface AudioProgressRepository {
    Optional<Long> findLastProcessedChunkId(Long userId, Long meetingId);

    void saveLastProcessedChunkId(Long userId, Long meetingId, Long chunkId);

    void deleteAudioProgress(Long userId, Long meetingId);

} 