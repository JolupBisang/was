package com.jolupbisang.demo.application.summary;

import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final MeetingAccessValidator meetingAccessValidator;
    private final MeetingSseService meetingSseService;

    public SseEmitter subscribe(Long meetingId, Long userId) {
        meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);

        return meetingSseService.subscribe(String.valueOf(meetingId), String.valueOf(userId), MeetingSseEventType.SUMMARY);
    }

    public void sendTestSummary(Long meetingId) {
        String testSummary = "Test summary for meeting ID: " + meetingId;
        meetingSseService.sendEventToMeeting(String.valueOf(meetingId), MeetingSseEventType.SUMMARY, testSummary);
    }
}
