package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.domain.meeting.event.MeetingCompletedEvent;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.service.ParticipationRateCalculator;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.participationRate.RealTimeParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    @Retryable(
            retryFor = {Exception.class}, // 모든 종류의 예외에 대해 재시도
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000) // 2초 후 재시작
    )
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMeetingCompletedEvent(MeetingCompletedEvent event) {
        log.info("참여율 저장 시작 - meetingId: {}", event.meetingId());

        Map<Long, Long> participationChunks = realTimeParticipationRepository.findByMeetingId(event.meetingId());

        if (participationChunks == null || participationChunks.isEmpty()) {
            log.warn("저장할 참여율 데이터가 없습니다 - meetingId: {}", event.meetingId());
            realTimeParticipationRepository.remove(event.meetingId());
            return;
        }

        Meeting meeting = meetingRepository.findByIdWithParticipant(event.meetingId())
                .orElseThrow(() -> new NotFoundException("meetingId: %d", event.meetingId()));

        Map<Long, Double> participationRates = participationRateCalculator.calculate(participationChunks);
        meeting.updateParticipationRates(participationRates, participationChunks);
    }

    @Recover
    public void recover(Exception e, MeetingCompletedEvent event) {

        log.error("참여율 저장 최종 실패 - meetingId: {}. 수동 처리 필요.", event.meetingId(), e);
    }
}
