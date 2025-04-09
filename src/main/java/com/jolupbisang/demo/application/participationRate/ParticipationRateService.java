package com.jolupbisang.demo.application.participationRate;

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

    private final MeetingSseService meetingSseService;

    public SseEmitter subscribe(Long meetingId, Long userId) {
        return meetingSseService.subscribe(String.valueOf(meetingId), String.valueOf(userId), MeetingSseEventType.PARTICIPATION_RATE);
    }

}
