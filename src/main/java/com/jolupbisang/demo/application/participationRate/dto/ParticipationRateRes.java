package com.jolupbisang.demo.application.participationRate.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ParticipationRateRes(
    LocalDateTime timestamp,
    Map<Long, Double> participationRates
) {

    public static ParticipationRateRes of(LocalDateTime timestamp, Map<Long, Double> participationRates) {
        return new ParticipationRateRes(timestamp, participationRates);
    }
}
