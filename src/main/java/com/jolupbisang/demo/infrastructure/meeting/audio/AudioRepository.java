package com.jolupbisang.demo.infrastructure.meeting.audio;

import com.jolupbisang.demo.application.audio.dto.AudioMeta;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public interface AudioRepository {
    String save(AudioMeta audioMeta, byte[] audioData) throws IOException;
}
