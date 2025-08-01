package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.event.whisper.WhisperDiarizedEvent;
import com.jolupbisang.demo.infrastructure.audio.client.dto.response.DiarizedResponse;
import com.jolupbisang.demo.infrastructure.participationRate.RealTimeParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiveParticipationRateUpdateService {

    private final RealTimeParticipationRepository realTimeParticipationRepository;

    @EventListener
    public void addToParticipationData(WhisperDiarizedEvent event) {
        DiarizedResponse diarizedResponse = event.getDiarizedResponse();
        long groupId = diarizedResponse.groupId();

        for (DiarizedResponse.Segment segment : diarizedResponse.completed()) {
            processSegment(groupId, segment);
        }
    }

    private void processSegment(long meetingId, DiarizedResponse.Segment segment) {
        if (segment.words() == null || segment.words().isEmpty()) {
            return;
        }

        DiarizedResponse.Word firstWord = segment.words().get(0);
        DiarizedResponse.Word lastWord = segment.words().get(segment.words().size() - 1);

        long participationDuration = lastWord.end() - firstWord.start();
        realTimeParticipationRepository.increaseParticipation(meetingId, segment.userId(), participationDuration);
    }

}
