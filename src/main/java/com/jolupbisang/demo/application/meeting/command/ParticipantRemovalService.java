package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.ParticipantRemovalRes;
import com.jolupbisang.demo.application.meeting.exception.MeetingNotFoundException;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantRemovalService {

    private final MeetingRepository meetingRepository;

    public ParticipantRemovalRes removeParticipant(long meetingId, long accessUserId, long participantId) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(MeetingNotFoundException::new);

        meeting.removeParticipant(accessUserId, participantId);

        return new ParticipantRemovalRes(meetingId, participantId);
    }
}
