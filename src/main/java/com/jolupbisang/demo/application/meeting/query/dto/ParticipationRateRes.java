package com.jolupbisang.demo.application.meeting.query.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ParticipationRateRes(
        List<UserParticipationRate> userParticipantRates
) {

    public static ParticipationRateRes of(Map<Long, Double> participationRates, Map<Long, String> nicknames) {
        List<UserParticipationRate> rates = new ArrayList<>();

        participationRates
                .forEach((userId, rate) -> {
                    String nickname = nicknames.get(userId);
                    rates.add(new UserParticipationRate(userId, nickname, rate));
                });

        return new ParticipationRateRes(rates);
    }

    private record UserParticipationRate(
            Long userId,
            String nickname,
            Double rate
    ) {
    }
}
