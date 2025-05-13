package com.jolupbisang.demo.infrastructure.meeting.audio;

import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public interface AudioRepository {
    void save(AudioMeta audioMeta, byte[] audioData) throws IOException;
}
