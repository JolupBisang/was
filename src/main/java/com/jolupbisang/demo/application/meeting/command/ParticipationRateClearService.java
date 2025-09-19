package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.domain.meeting.event.ParticipationRateSavedEvent;
import com.jolupbisang.demo.infrastructure.participationRate.RealTimeParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ParticipationRateClearService {

    private final RealTimeParticipationRepository participationRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleParticipationRateSavedEvent(ParticipationRateSavedEvent event) {
        long meetingId = event.meetingId();
        participationRepository.remove(meetingId);
    }
}
