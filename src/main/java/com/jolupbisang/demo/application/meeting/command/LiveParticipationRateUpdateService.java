package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.segment.event.CompletedSegmentReceivedEvent;
import com.jolupbisang.demo.application.segment.event.SegmentDto;
import com.jolupbisang.demo.application.segment.event.WordDto;
import com.jolupbisang.demo.infrastructure.participationRate.RealTimeParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiveParticipationRateUpdateService {

    private final RealTimeParticipationRepository realTimeParticipationRepository;

    @EventListener
    public void addToParticipationData(CompletedSegmentReceivedEvent event) {
        for (SegmentDto segmentDto : event.completed()) {
            processSegment(event.meetingId(), segmentDto);
        }
    }

    private void processSegment(long meetingId, SegmentDto segment) {
        if (segment.words() == null || segment.words().isEmpty()) {
            return;
        }

        WordDto firstWord = segment.words().get(0);
        WordDto lastWord = segment.words().get(segment.words().size() - 1);


        long participationDuration = lastWord.end() - firstWord.start();
        realTimeParticipationRepository.increaseParticipation(meetingId, segment.userId(), participationDuration);
    }

}
