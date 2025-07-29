package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.MeetingStatusChangeRes;
import com.jolupbisang.demo.application.meeting.command.dto.TargetMeetingStatus;
import com.jolupbisang.demo.application.meeting.exception.MeetingErrorCode;
import com.jolupbisang.demo.application.meeting.exception.NoSuchMeetingStatusException;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.ServiceLogicException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MeetingStatusChangeService {

    private final MeetingRepository meetingRepository;

    @Transactional
    public MeetingStatusChangeRes changeMeetingStatus(long meetingId, long accessId, TargetMeetingStatus targetMeetingStatus) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new ServiceLogicException(MeetingErrorCode.MEETING_NOT_FOUND));

        switch (targetMeetingStatus) {
            case IN_PROGRESSING -> meeting.start(accessId);
            case COMPLETED -> meeting.complete(accessId);
            case CANCELLED -> meeting.cancel(accessId);
            default -> throw new NoSuchMeetingStatusException(Map.of("targetMeetingStatus", targetMeetingStatus));
        }

        return new MeetingStatusChangeRes(meetingId, targetMeetingStatus.toString());
    }
}
