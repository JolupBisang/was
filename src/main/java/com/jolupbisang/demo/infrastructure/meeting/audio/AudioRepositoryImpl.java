package com.jolupbisang.demo.infrastructure.meeting.audio;

import com.jolupbisang.demo.application.audio.dto.AudioMeta;
import com.jolupbisang.demo.infrastructure.aws.s3.S3ClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AudioRepositoryImpl implements AudioRepository {

    private final S3ClientUtil s3ClientUtil;

    @Override
    public String save(AudioMeta audioMeta, byte[] audioData) throws IOException {
        String s3Key = generateS3ChunkKey(audioMeta);

        String contentType = "audio/pcm";
        if (audioMeta.encoding() != null && !audioMeta.encoding().trim().isEmpty()) {
            if (audioMeta.encoding().toLowerCase().startsWith("audio/")) {
                contentType = audioMeta.encoding();
            }
        }

        String audioURL = null;
        try (InputStream audioInputStream = new ByteArrayInputStream(audioData)) {
            audioURL = s3ClientUtil.uploadInputStream(s3Key, audioInputStream, audioData.length, contentType);
            log.debug("Saved audio chunk to S3: {}", s3Key);
        } catch (IOException e) {
            log.error("Failed to save audio chunk {} to S3 for meetingId: {}, userId: {}.",
                    s3Key, audioMeta.meetingId(), audioMeta.userId(), e);
            throw e;
        }

        return audioURL;
    }

    @Override
    public String findCompletedURLByMeetingIdAndUserId(long meetingId, long userId, Duration duration) {
        return s3ClientUtil.generatePresignedUrl(
                generateS3CompletedURLKey(meetingId, userId),
                duration
        );
    }

    @Override
    public List<Long> findCompletedUserIdsByMeetingId(long meetingId) {
        String prefix = String.format("merged-audio/meeting-%d/", meetingId);
        List<String> objectKeys = s3ClientUtil.listObjectKeysByPrefix(prefix);

        Pattern userIdPattern = Pattern.compile("merged-audio/meeting-\\d+/user-(\\d+)/");

        return objectKeys.stream()
                .map(userIdPattern::matcher)
                .filter(Matcher::find)
                .map(matcher -> Long.parseLong(matcher.group(1)))
                .distinct()
                .toList();
    }

    private String generateS3ChunkKey(AudioMeta audioMeta) {
        return String.format("pending-chunks/meeting-%d/user-%d/%d.pcm",
                audioMeta.meetingId(), audioMeta.userId(), audioMeta.chunkId());
    }

    private String generateS3CompletedURLKey(long meetingId, long userId) {
        return String.format("merged-audio/meeting-%d/user-%d/merged.opus",
                meetingId, userId);
    }
}
