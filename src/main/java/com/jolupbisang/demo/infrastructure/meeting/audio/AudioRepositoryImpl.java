package com.jolupbisang.demo.infrastructure.meeting.audio;

import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import com.jolupbisang.demo.global.properties.MeetingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AudioRepositoryImpl implements AudioRepository {
    
    private final MeetingProperties meetingProperties;
    
    @Override
    public void save(AudioMeta audioMeta, byte[] audioData) throws IOException {
        Path dirPath = Path.of(meetingProperties.getBaseDir(),
                Long.toString(audioMeta.meetingId()),
                Long.toString(audioMeta.userId()));
        String filename = Integer.toString(audioMeta.chunkId());

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        File chunkFile = dirPath.resolve(filename).toFile();

        try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
            fos.write(audioData);
        } catch (IOException e) {
            log.error("[Failed to save audio file] meetingId: {}, userId:{}, chunkId:{} ", 
                    audioMeta.meetingId(), audioMeta.userId(), audioMeta.chunkId(), e);
            throw e;
        }
    }
}
