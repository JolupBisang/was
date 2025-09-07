package com.jolupbisang.demo.application.meeting.service;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    //todo: afterConnectionEstablished 에서 쓰고 있는데 삭제하고 리팩토링
    public LocalDateTime getMeetingStartTime(long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));
        return meeting.getScheduledTime().getScheduledStartTime();
    }
}
