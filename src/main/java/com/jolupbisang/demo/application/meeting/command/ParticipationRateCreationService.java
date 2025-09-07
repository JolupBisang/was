package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.domain.meeting.event.MeetingCompletedEvent;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.service.ParticipationRateCalculator;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.participationRate.RealTimeParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRateCreationService {

    private final MeetingRepository meetingRepository;
    private final RealTimeParticipationRepository realTimeParticipationRepository;
    private final ParticipationRateCalculator participationRateCalculator;

    @Order(2)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void saveParticipationRate(MeetingCompletedEvent event) {
        saveParticipationRates(event.meetingId());
        realTimeParticipationRepository.remove(event.meetingId());
    }

    private void saveParticipationRates(Long meetingId) {
        Map<Long, Long> participationChunks = realTimeParticipationRepository.findByMeetingId(meetingId);

        if (participationChunks == null || participationChunks.isEmpty()) {
            log.error("저장할 참여율 데이터가 없습니다 - meetingId: {}", meetingId);
            return;
        }

        try {
            Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                    .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

            Map<Long, Double> participationRates = participationRateCalculator.calculate(participationChunks);
            meeting.updateParticipationRates(participationRates, participationChunks);
        } catch (Exception e) {
            log.error("참여율 데이터 저장 실패 - meetingId: {}", meetingId, e);
        }
    }
}
