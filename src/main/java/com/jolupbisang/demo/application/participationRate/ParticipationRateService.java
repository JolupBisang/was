package com.jolupbisang.demo.application.participationRate;

import com.jolupbisang.demo.application.meeting.exception.MeetingErrorCode;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.meeting.MeetingStatus;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRateService {

    private final MeetingRepository meetingRepository;
    private final MeetingSseService meetingSseService;

    public SseEmitter subscribe(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.NOT_FOUND));

        if (!meeting.getMeetingStatus().equals(MeetingStatus.IN_PROGRESS)) {
            throw new CustomException(MeetingErrorCode.NOT_IN_PROGRESS);
        }

        return meetingSseService.subscribe(String.valueOf(meetingId), String.valueOf(userId), MeetingSseEventType.PARTICIPATION_RATE);
    }

}
