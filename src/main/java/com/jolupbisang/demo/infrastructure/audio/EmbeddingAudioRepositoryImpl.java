package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.infrastructure.aws.s3.S3ClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Repository
public class EmbeddingAudioRepositoryImpl implements EmbeddingAudioRepository {

    private final S3ClientUtil s3ClientUtil;

    public void save(long userId, byte[] audio) {
        String s3Key = generateS3Key(userId);
        String contentType = "audio/mp4";

        try (InputStream audioInputStream = new ByteArrayInputStream(audio)) {
            s3ClientUtil.uploadInputStream(s3Key, audioInputStream, audio.length, contentType);
        } catch (IOException e) {
            log.error("Failed to save audio embedding {} to S3 for userId: {}.",
                    s3Key, userId, e);
        }
    }

    private String generateS3Key(long userId) {
        return String.format("embedding/user-%d/%s", userId, System.currentTimeMillis());
    }
}
