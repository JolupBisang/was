package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.MeetingDetailUpdateRes;
import com.jolupbisang.demo.application.meeting.exception.MeetingNotFoundException;
import com.jolupbisang.demo.domain.meeting.dto.MeetingDetailUpdateDto;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.RestTime;
import com.jolupbisang.demo.domain.meeting.model.ScheduledTime;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingDetailUpdateService {

    private final MeetingRepository meetingRepository;

    @Transactional
    public MeetingDetailUpdateRes updateDetail(long meetingId, long accessUserId, MeetingUpdateReq meetingUpdateReq) {
        MeetingDetailUpdateDto meetingDetailUpdateDto = createMeetingDetailUpdateDto(meetingUpdateReq);

        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(MeetingNotFoundException::new);

        meeting.updateDetails(meetingDetailUpdateDto, accessUserId);

        return new MeetingDetailUpdateRes(meeting.getId());
    }

    private MeetingDetailUpdateDto createMeetingDetailUpdateDto(MeetingUpdateReq meetingUpdateReq) {
        return new MeetingDetailUpdateDto(
                meetingUpdateReq.title(),
                meetingUpdateReq.location(),
                new ScheduledTime(meetingUpdateReq.scheduledStartTime(), meetingUpdateReq.targetTime()),
                new RestTime(meetingUpdateReq.restInterval(), meetingUpdateReq.restDuration())
        );
    }
}
