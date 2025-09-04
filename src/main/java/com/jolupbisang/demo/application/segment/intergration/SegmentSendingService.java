package com.jolupbisang.demo.application.segment.intergration;

import com.jolupbisang.demo.application.segment.event.CandidateSegmentReceivedEvent;
import com.jolupbisang.demo.application.segment.event.CompletedSegmentReceivedEvent;
import com.jolupbisang.demo.application.segment.event.SegmentDto;
import com.jolupbisang.demo.application.segment.intergration.dto.RealtimeSegmentDto;
import com.jolupbisang.demo.domain.segment.service.SegmentTimeCalculator;
import com.jolupbisang.demo.infrastructure.audio.AudioProgressRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingWebsocketManager;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SegmentSendingService {

    private final MeetingWebsocketManager websocketManager;
    private final SegmentTimeCalculator segmentTimeCalculator;
    private final AudioProgressRepository audioProgressRepository;

    @EventListener
    public void sendCandidateToMeetingUser(CandidateSegmentReceivedEvent event) {
        for (SegmentDto segmentDto : event.candidate()) {
            RealtimeSegmentDto realtimeSegmentDto = createSegmentDto(event.meetingId(), segmentDto);
            websocketManager.sendToMeeting(event.meetingId(), SocketResponseType.DIARIZED_SEGMENT, realtimeSegmentDto);
        }
    }

    @EventListener
    public void sendCompletedToMeetingUser(CompletedSegmentReceivedEvent event) {
        for (SegmentDto segmentDto : event.completed()) {
            RealtimeSegmentDto realtimeSegmentDto = createSegmentDto(event.meetingId(), segmentDto);
            websocketManager.sendToMeeting(event.meetingId(), SocketResponseType.DIARIZED_SEGMENT, realtimeSegmentDto);
        }
    }

    public RealtimeSegmentDto createSegmentDto(long meetingId, SegmentDto segmentDto) {
        LocalDateTime firstProcessedTime = audioProgressRepository.findFirstProcessedTime(meetingId)
                .orElse(null);

        return new RealtimeSegmentDto(
                segmentTimeCalculator.calculateSpokenTime(segmentDto, firstProcessedTime),
                segmentDto.userId(),
                segmentDto.order(),
                segmentDto.text()
        );
    }
}
