package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.domain.audio.model.FullAudio;

import java.time.Duration;
import java.util.List;

public interface FullAudioRepository {
    List<FullAudio> findAllByMeetingId(long meetingId);

    FullAudio findByMeetingIdAndUserId(long meetingId, long userId, Duration duration);
}
