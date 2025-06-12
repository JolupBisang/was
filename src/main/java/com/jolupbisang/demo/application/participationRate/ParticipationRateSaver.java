package com.jolupbisang.demo.application.participationRate;

import com.jolupbisang.demo.domain.participationRate.ParticipationRate;
import com.jolupbisang.demo.infrastructure.participationRate.ParticipationRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParticipationRateSaver {

    private final ParticipationRateRepository participationRateRepository;

    @Async("AsyncTaskExecutor")
    @Transactional
    public void saveAll(List<ParticipationRate> participationRates) {
        participationRateRepository.saveAll(participationRates);
    }
}
