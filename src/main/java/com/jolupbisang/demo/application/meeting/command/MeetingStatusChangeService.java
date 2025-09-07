package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.MeetingStatusChangeRes;
import com.jolupbisang.demo.application.meeting.command.dto.MeetingStatusUpdateReq;
import com.jolupbisang.demo.application.meeting.exception.MeetingApplicationErrorCode;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingStatusChangeService {

    private final MeetingRepository meetingRepository;

    @Transactional
    public MeetingStatusChangeRes changeMeetingStatus(long meetingId, long accessId, MeetingStatusUpdateReq meetingStatusUpdateReq) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        switch (meetingStatusUpdateReq.targetStatus()) {
            case IN_PROGRESSING -> meeting.start(accessId);
            case COMPLETED -> meeting.complete(accessId);
            case CANCELLED -> meeting.cancel(accessId);
            default ->
                    throw new ApplicationException(MeetingApplicationErrorCode.NO_SUCH_MEETING_STATUS, "targetStatus: %s", meetingStatusUpdateReq.targetStatus());
        }

        return new MeetingStatusChangeRes(meetingId, meetingStatusUpdateReq.targetStatus().toString());
    }
}
