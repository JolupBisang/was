package com.jolupbisang.demo.application.audio.intergration;

import com.jolupbisang.demo.domain.meeting.event.MeetingStartedEvent;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.Participant;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.audio.EmbeddedVectorRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.whisper.WhisperClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddedVectorSendingService {

    private final EmbeddedVectorRepository embeddedVectorRepository;
    private final MeetingRepository meetingRepository;
    private final WhisperClient whisperClient;

    @EventListener
    public void handleMeetingStartingEvent(MeetingStartedEvent event) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(event.meetingId())
                .orElseThrow(() -> new NotFoundException("meetingId: %d", event.meetingId()));

        List<Long> userIds = meeting.getParticipants()
                .stream().map(Participant::getUserId)
                .toList();

        List<byte[]> totalVectors = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (Long userId : userIds) {
            List<byte[]> userVectors = embeddedVectorRepository.findAllByUserId(userId);
            counts.add(userVectors.size());
            totalVectors.addAll(userVectors);
        }

        whisperClient.sendRefenceVector(event.meetingId(), userIds, counts, totalVectors);
    }
}
