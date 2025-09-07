package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.domain.meeting.exception.NotParticipantException;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class MeetingEventSubscribeService {

    private final MeetingRepository meetingRepository;
    private final MeetingSseManager meetingSseManager;

    public SseEmitter subscribe(long meetingId, long userId) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        if (!meeting.isParticipant(userId)) {
            throw new NotParticipantException();
        }

        return meetingSseManager.subscribe(meetingId, userId);
    }
}
