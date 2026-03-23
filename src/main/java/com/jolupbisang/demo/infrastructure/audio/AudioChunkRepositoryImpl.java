package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.domain.audio.model.AudioChunk;
import com.jolupbisang.demo.global.exception.InfraException;
import com.jolupbisang.demo.infrastructure.audio.exception.AudioInfraErrorCode;
import com.jolupbisang.demo.infrastructure.aws.s3.S3ClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Repository
@RequiredArgsConstructor
public class AudioChunkRepositoryImpl implements AudioChunkRepository {

    private final S3ClientUtil s3ClientUtil;

    private static final String S3_AUDIO_CHUNK_KEY_FORMAT = "pending-chunks/meeting-%d/user-%d/%d";

    @Override
    public String save(AudioChunk audioChunk) {
        String s3Key = generateS3ChunkKey(audioChunk);

        String audioURL;
        try (InputStream audioInputStream = new ByteArrayInputStream(audioChunk.getAudioData())) {
            audioURL = s3ClientUtil.uploadInputStream(s3Key, audioInputStream, audioChunk.getAudioData().length, audioChunk.getEncodingType().getType());
        } catch (IOException e) {
            throw new InfraException(AudioInfraErrorCode.AUDIO_STORAGE_FAILED, e, "meetingId: %d, userId: %d, chunkId: %d", audioChunk.getMeetingId(), audioChunk.getUserId(), audioChunk.getChunkId());
        }

        return audioURL;
    }


    private String generateS3ChunkKey(AudioChunk audioChunk) {
        String keyFormat = S3_AUDIO_CHUNK_KEY_FORMAT + audioChunk.getEncodingType().getExtension();
        return String.format(keyFormat, audioChunk.getMeetingId(), audioChunk.getUserId(), audioChunk.getChunkId());
    }
}
