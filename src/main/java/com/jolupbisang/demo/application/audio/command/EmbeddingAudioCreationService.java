package com.jolupbisang.demo.application.audio.command;

import com.jolupbisang.demo.application.audio.exception.UnaccessibleAudioFileException;
import com.jolupbisang.demo.domain.audio.model.AudioEncodingType;
import com.jolupbisang.demo.domain.audio.model.EmbeddingAudio;
import com.jolupbisang.demo.infrastructure.audio.EmbeddingAudioRepository;
import com.jolupbisang.demo.infrastructure.whisper.WhisperClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmbeddingAudioCreationService {

    private final EmbeddingAudioRepository embeddingAudioRepository;
    private final WhisperClient whisperClient;

    @Transactional
    public void createEmbeddingAudio(long userId, MultipartFile audioFile) {
        byte[] audioBytes;
        try {
            audioBytes = audioFile.getBytes();
        } catch (IOException e) {
            log.error("AudioFile Access Error: {}", e.getMessage(), e);
            throw new UnaccessibleAudioFileException(Map.of("audioType", audioFile.getContentType()), e);
        }

        whisperClient.sendEmbeddingAudio(userId, audioBytes);
        embeddingAudioRepository.save(new EmbeddingAudio(
                userId,
                AudioEncodingType.fromString(audioFile.getContentType()),
                LocalDateTime.now(),
                audioBytes)
        );
    }
}
