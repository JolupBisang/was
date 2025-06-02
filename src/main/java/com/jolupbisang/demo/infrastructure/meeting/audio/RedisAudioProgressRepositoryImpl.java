package com.jolupbisang.demo.infrastructure.meeting.audio;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisAudioProgressRepositoryImpl implements AudioProgressRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX_CHUNK = "lastProcessedChunkId:";
    private static final String KEY_PREFIX_FIRST_AUDIO_TIME = "firstProcessedChunkTime:";

    @Override
    public Optional<Long> findLastProcessedChunkId(Long userId, Long meetingId) {
        String key = generateLastProcessedKey(userId, meetingId);
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public void saveLastProcessedChunkId(Long userId, Long meetingId, Long chunkId, LocalDateTime lastProcessedTime) {
        redisTemplate.opsForValue().set(generateLastProcessedKey(userId, meetingId), String.valueOf(chunkId));
        redisTemplate.opsForValue().setIfAbsent(generateFirstProcessedKey(meetingId), lastProcessedTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public void deleteAudioProgress(Long userId, Long meetingId) {
        redisTemplate.delete(generateLastProcessedKey(userId, meetingId));
        redisTemplate.delete(generateFirstProcessedKey(meetingId));
    }

    @Override
    public Optional<LocalDateTime> findFirstProcessedTime(Long meetingId) {
        String value = redisTemplate.opsForValue().get(generateFirstProcessedKey(meetingId));

        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    private String generateLastProcessedKey(Long userId, Long meetingId) {
        return KEY_PREFIX_CHUNK + userId + ":" + meetingId;
    }

    private String generateFirstProcessedKey(Long meetingId) {
        return KEY_PREFIX_FIRST_AUDIO_TIME + meetingId;
    }
} 
