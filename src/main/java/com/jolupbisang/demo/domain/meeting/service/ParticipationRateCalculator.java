package com.jolupbisang.demo.domain.meeting.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ParticipationRateCalculator {

    public Map<Long, Double> calculate(Map<Long, Long> participationChunks) {
        long totalTime = participationChunks.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        Map<Long, Double> participationRates = new HashMap<>();
        for (Map.Entry<Long, Long> entry : participationChunks.entrySet()) {
            double participationRate = totalTime > 0 ? (double) entry.getValue() / totalTime * 100 : 0.0;
            participationRates.put(entry.getKey(), Math.round(participationRate * 100.0) / 100.0);
        }

        return participationRates;
    }
}
