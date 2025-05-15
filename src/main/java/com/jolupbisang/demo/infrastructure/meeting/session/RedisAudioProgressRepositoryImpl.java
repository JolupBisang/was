package com.jolupbisang.demo.infrastructure.meeting.session;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisAudioProgressRepositoryImpl implements AudioProgressRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "audioprogress:";

    @Override
    public Optional<Long> findLastProcessedChunkId(Long userId, Long meetingId) {
        String key = generateKey(userId, meetingId);
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
    public void saveLastProcessedChunkId(Long userId, Long meetingId, Long chunkId) {
        String key = generateKey(userId, meetingId);
        redisTemplate.opsForValue().set(key, String.valueOf(chunkId));
    }

    @Override
    public void deleteAudioProgress(Long userId, Long meetingId) {
        String key = generateKey(userId, meetingId);
        redisTemplate.delete(key);
    }


    private String generateKey(Long userId, Long meetingId) {
        return KEY_PREFIX + userId + ":" + meetingId;
    }
} 