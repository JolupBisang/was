package com.jolupbisang.demo.application.audio.command;

import com.jolupbisang.demo.application.audio.command.dto.AudioChunkReq;
import com.jolupbisang.demo.domain.audio.model.AudioChunk;
import com.jolupbisang.demo.domain.audio.model.AudioEncodingType;
import com.jolupbisang.demo.infrastructure.audio.AudioChunkRepository;
import com.jolupbisang.demo.infrastructure.audio.AudioProgressRepository;
import com.jolupbisang.demo.infrastructure.whisper.WhisperClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioChunkReceiveService {

    private final AudioChunkRepository audioRepository;
    private final AudioProgressRepository audioProgressRepository;
    private final WhisperClient whisperClient;

    private static final Integer AUDIO_SAMPLE_RATE = 16000;

    public void receiveAudioChunk(long meetingId, long userId, AudioChunkReq chunkReq) {

        Integer offset = calculateOffsetIfNeeded(meetingId, userId, chunkReq.timestamp());
        whisperClient.sendDiarization(meetingId, userId, offset, chunkReq.audioData());

        audioRepository.save(createAudioChunk(meetingId, userId, chunkReq));
        audioProgressRepository.saveLastProcessedChunkId(userId, meetingId, chunkReq.chunkId(), chunkReq.timestamp());
    }

    private AudioChunk createAudioChunk(long meetingId, long userId, AudioChunkReq chunkReq) {
        return new AudioChunk(
                meetingId,
                userId,
                chunkReq.chunkId(),
                AudioEncodingType.fromString(chunkReq.encoding()),
                chunkReq.timestamp(),
                chunkReq.audioData()
        );
    }

    private Integer calculateOffsetIfNeeded(long meetingId, long userId, LocalDateTime timestamp) {
        // 첫 번째 청크인지 확인
        if (!audioProgressRepository.existFirstChunkFlag(meetingId, userId)) {
            audioProgressRepository.setFirstChunkFlag(meetingId, userId);

            // 첫 처리 시간을 기준으로 오프셋 계산
            return audioProgressRepository.findFirstProcessedTime(meetingId)
                    .map(firstTime -> calculateOffset(firstTime, timestamp))
                    .orElse(null);
        }
        return null;
    }

    private Integer calculateOffset(LocalDateTime firstProcessedTime, LocalDateTime timestamp) {
        long secondsDifference = Duration.between(firstProcessedTime, timestamp).getSeconds();

        try {
            int intSecondsDifference = Math.toIntExact(secondsDifference);
            return intSecondsDifference * AUDIO_SAMPLE_RATE;
        } catch (ArithmeticException e) {
            log.error("Offset calculation overflow - seconds difference: {}", secondsDifference);
            return null;
        }
    }
}
