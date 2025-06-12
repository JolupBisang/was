package com.jolupbisang.demo.infrastructure.participationRate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Repository
public class RealTimeParticipationRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void increaseParticipation(long meetingId, long userId, long chunkSize) {
        redisTemplate.opsForValue().increment("participationRate:" + meetingId + ":" + userId, chunkSize);
    }

    public Map<Long, Long> findByMeetingId(long meetingId) {
        String pattern = "participationRate:" + meetingId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);

        Map<Long, Long> participationMap = new HashMap<>();

        for (String key : keys) {
            String[] keyParts = key.split(":");
            if (keyParts.length == 3) {
                try {
                    Long userId = Long.parseLong(keyParts[2]);
                    String participationValue = redisTemplate.opsForValue().get(key);
                    if (participationValue != null) {
                        participationMap.put(userId, Long.parseLong(participationValue));
                    }
                } catch (NumberFormatException e) {
                    log.error("[meetingId: {}] 참여율 조회시 문제 발생", meetingId, e);
                }
            }
        }

        return participationMap;
    }

    public void remove(Long meetingId) {
        String pattern = "participationRate:" + meetingId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);

        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }
}
