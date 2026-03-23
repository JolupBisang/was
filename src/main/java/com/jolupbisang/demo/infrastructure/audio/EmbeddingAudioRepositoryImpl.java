package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.domain.audio.model.EmbeddingAudio;
import com.jolupbisang.demo.global.exception.InfraException;
import com.jolupbisang.demo.infrastructure.audio.exception.AudioInfraErrorCode;
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

    private static final String S3_KEY_FORMAT = "embedding/user-%d/%s";

    private final S3ClientUtil s3ClientUtil;

    public void save(EmbeddingAudio embeddingAudio) {
        String s3Key = generateS3Key(embeddingAudio.getUserId(), embeddingAudio.getCreatedDateTime().toString());
        String contentType = embeddingAudio.getEncodingType().getType();

        try (InputStream audioInputStream = new ByteArrayInputStream(embeddingAudio.getAudio())) {
            s3ClientUtil.uploadInputStream(s3Key, audioInputStream, embeddingAudio.getAudio().length, contentType);
        } catch (IOException ex) {
            log.error("Failed to save audio embedding {} to S3 for userId: {}.", s3Key, embeddingAudio.getUserId(), ex);
            throw new InfraException(AudioInfraErrorCode.AUDIO_STORAGE_FAILED, ex, "userId: %d, s3Key: %s", embeddingAudio.getUserId(), s3Key);
        }
    }

    private String generateS3Key(long userId, String timestamp) {
        return String.format(S3_KEY_FORMAT, userId, timestamp);
    }
}
