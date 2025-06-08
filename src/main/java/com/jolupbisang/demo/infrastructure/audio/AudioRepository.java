package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.application.audio.dto.AudioMeta;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Repository
public interface AudioRepository {
    String save(AudioMeta audioMeta, byte[] audioData) throws IOException;

    String findCompletedURLByMeetingIdAndUserId(long meetingId, long userId, Duration duration);

    List<Long> findCompletedUserIdsByMeetingId(long meetingId);
}
