package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.application.meeting.query.dto.DuplicationCheckRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.MeetingStatus;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MeetingDuplicatedTimeCheckService {

    private final MeetingRepository meetingRepository;

    @Transactional(readOnly = true)
    public DuplicationCheckRes checkDuplicatedTime(LocalDateTime startTime, long targetMinutes, long userId) {

        List<Meeting> existedMeeting = meetingRepository.findByScheduledTimeAndParticipantAndStatuses(startTime, startTime.plusMinutes(targetMinutes), userId, Set.of(MeetingStatus.WAITING, MeetingStatus.IN_PROGRESS));

        return DuplicationCheckRes.of(existedMeeting);
    }
}
