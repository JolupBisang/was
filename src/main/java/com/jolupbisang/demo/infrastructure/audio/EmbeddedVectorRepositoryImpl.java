package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.infrastructure.aws.s3.S3ClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmbeddedVectorRepositoryImpl implements EmbeddedVectorRepository {
    private final S3ClientUtil s3ClientUtil;

    @Override
    public void save(long userId, byte[] audio) throws IOException {
        String s3Key = generateS3Key(userId);
        String contentType = "application/octet-stream";

        try (InputStream audioInputStream = new ByteArrayInputStream(audio)) {
            s3ClientUtil.uploadInputStream(s3Key, audioInputStream, audio.length, contentType);
        } catch (IOException e) {
            log.error("Failed to save audio embeddedVector {} to S3 for userId: {}.", s3Key, userId, e);
            throw e;
        }
    }

    @Override
    public List<byte[]> findAllByUserId(long userId) {
        String s3KeyPrefix = generateS3Prefix(userId);
        List<String> objectKeys = s3ClientUtil.listObjectKeysByPrefix(s3KeyPrefix);
        List<byte[]> audioDataList = new ArrayList<>();

        for (String key : objectKeys) {
            try {
                byte[] audioData = s3ClientUtil.downloadObjectAsByteArray(key);
                audioDataList.add(audioData);
            } catch (IOException e) {
                log.error("Failed to download audio file {} from S3 for userId: {}. Skipping this file.", key, userId, e);
            }
        }
        return audioDataList;
    }


    private String generateS3Key(long userId) {
        return String.format("embedded/user-%d/%s", userId, System.currentTimeMillis());
    }

    private String generateS3Prefix(long userId) {
        return String.format("embedded/user-%d/", userId);
    }


}
