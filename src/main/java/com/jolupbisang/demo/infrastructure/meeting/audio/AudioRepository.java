package com.jolupbisang.demo.infrastructure.meeting.audio;

import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Repository
public interface AudioRepository {
    void save(AudioMeta audioMeta, byte[] audioData) throws IOException;

    List<Path> getAllAudioChunkPaths(Long meetingId, Long userId) throws IOException;

    List<Long> getAllUserIdByMeetingId(Long meetingId) throws IOException;
}
