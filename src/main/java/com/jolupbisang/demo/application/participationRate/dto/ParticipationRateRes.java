package com.jolupbisang.demo.application.participationRate.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ParticipationRateRes(
    LocalDateTime timestamp,
    List<RatePerUser> participationRates
) {

    public static ParticipationRateRes of(LocalDateTime timestamp, Map<Long, Double> participationRatesMap) {
        List<RatePerUser> participationRates = participationRatesMap.entrySet().stream()
                .map(entry -> new RatePerUser(entry.getKey(), entry.getValue()))
                .toList();
        return new ParticipationRateRes(timestamp, participationRates);
    }

    public record RatePerUser(
            Long userId,
            Double rate
    ) {
    }
}
