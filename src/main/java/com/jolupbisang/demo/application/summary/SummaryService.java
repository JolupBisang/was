package com.jolupbisang.demo.application.summary;

import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.event.SummaryReceivedEvent;
import com.jolupbisang.demo.application.summary.dto.SseSummaryRes;
import com.jolupbisang.demo.application.summary.dto.SummaryListRes;
import com.jolupbisang.demo.application.summary.exception.SummaryErrorCode;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.summary.Summary;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseService;
import com.jolupbisang.demo.infrastructure.summary.SummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final MeetingSseService meetingSseService;
    private final MeetingRepository meetingRepository;
    private final SummaryRepository summaryRepository;

    private final MeetingAccessValidator meetingAccessValidator;


    public SseEmitter subscribe(Long meetingId, Long userId) {
        meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);

        return meetingSseService.subscribe(String.valueOf(meetingId), String.valueOf(userId), MeetingSseEventType.SUMMARY);
    }

    public Slice<SummaryListRes> getSummaries(Long meetingId, Long userId, boolean isRecap, Pageable pageable) {
        meetingAccessValidator.validateUserParticipating(meetingId, userId);

        Slice<Summary> summaries = summaryRepository.findByMeetingIdAndIsRecap(meetingId, isRecap, pageable);

        return summaries.map(SummaryListRes::from);
    }

    @EventListener
    public void handleSummaryReceived(SummaryReceivedEvent event) {
        long meetingId = event.getMeetingId();
        String summary = event.getContext();
        boolean isRecap = event.isRecap();
        LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault());

        Meeting meeting;
        try {
            meeting = meetingRepository.findById(meetingId)
                    .orElseThrow(() -> new CustomException(SummaryErrorCode.MEETING_NOT_FOUND));
        } catch (CustomException e) {
            log.error("[Whisper Feedback Error] {}", e.getErrorCode().getMessage(), e);
            return;
        }

        summaryRepository.save(new Summary(meeting, summary, isRecap, timestamp));
        meetingSseService.sendEventToMeeting(String.valueOf(meetingId), MeetingSseEventType.SUMMARY, SseSummaryRes.of(timestamp, summary));
    }
}
