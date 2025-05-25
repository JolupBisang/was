package com.jolupbisang.demo.infrastructure.meeting.audio;

import com.jolupbisang.demo.application.meeting.dto.AudioMeta;
import com.jolupbisang.demo.global.properties.MeetingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        String filename = Long.toString(audioMeta.chunkId());

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

    @Override
    public List<Path> getAllAudioChunkPaths(Long meetingId, Long userId) throws IOException {
        Path userAudioDir = getUserAudioChunksDirectory(meetingId, userId);
        List<Path> audioChunks = new ArrayList<>();

        if (Files.exists(userAudioDir) && Files.isDirectory(userAudioDir)) {
            try (DirectoryStream<Path> chunkFiles = Files.newDirectoryStream(userAudioDir)) {
                for (Path chunkFile : chunkFiles) {
                    if (Files.isRegularFile(chunkFile)) {
                        audioChunks.add(chunkFile);
                    }
                }
            }
        } else {
            log.warn("Audio chunk directory not found or not a directory: {}", userAudioDir);
            return Collections.emptyList();
        }
        return audioChunks;
    }

    @Override
    public List<Long> getAllUserIdByMeetingId(Long meetingId) throws IOException {
        Path meetingAudioBasePath = Path.of(meetingProperties.getBaseDir(), String.valueOf(meetingId));
        if (!Files.exists(meetingAudioBasePath) || !Files.isDirectory(meetingAudioBasePath)) {
            log.warn("Audio base directory not found for meetingId: {}. No participants with audio.", meetingId);
            return Collections.emptyList();
        }

        List<Long> userIds = new ArrayList<>();
        try (DirectoryStream<Path> userAudioDirs = Files.newDirectoryStream(meetingAudioBasePath)) {
            for (Path userAudioDir : userAudioDirs) {
                if (Files.isDirectory(userAudioDir)) {
                    try {
                        userIds.add(Long.parseLong(userAudioDir.getFileName().toString()));
                    } catch (NumberFormatException e) {
                        log.warn("Invalid user ID directory name: {}", userAudioDir.getFileName(), e);
                    }
                }
            }
        }
        return userIds;
    }

    private Path getUserAudioChunksDirectory(Long meetingId, Long userId) {
        return Path.of(meetingProperties.getBaseDir(),
                Long.toString(meetingId),
                Long.toString(userId));
    }
}
