package com.jolupbisang.demo.domain.meeting.service;

import com.jolupbisang.demo.domain.meeting.entity.Meeting;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MeetingCreationService {

    private final MeetingRepository meetingRepository;

    public Meeting createByEmail(String title, String location, LocalDateTime scheduledStartTime, int targetTime, int restInterval, int restDuration) {
        Meeting meeting = new Meeting(
                title,
                location,
                scheduledStartTime,
                targetTime,
                restInterval,
                restDuration
        );

        return meetingRepository.save(meeting);
    }
}
