package com.jolupbisang.demo.application.participationRate.dto;

import com.jolupbisang.demo.domain.participationRate.ParticipationRate;

import java.util.List;

public record ParticipationRateHistoryRes(
        List<UserParticipationRate> userParticipationRates
) {

    public static ParticipationRateHistoryRes of(List<ParticipationRate> participationRates) {
        List<UserParticipationRate> userRates = participationRates.stream()
                .map(pr -> new UserParticipationRate(
                        pr.getUser().getId(),
                        pr.getUser().getNickname(),
                        pr.getRate()
                ))
                .toList();

        return new ParticipationRateHistoryRes(userRates);
    }

    public record UserParticipationRate(
            Long userId,
            String nickname,
            Double rate
    ) {
    }
} 