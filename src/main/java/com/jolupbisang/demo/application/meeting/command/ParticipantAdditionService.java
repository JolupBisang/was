package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.ParticipantAdditionRes;
import com.jolupbisang.demo.domain.meeting.dto.ParticipantAddReq;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.MeetingRole;
import com.jolupbisang.demo.domain.meeting.model.ParticipantDetail;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantAdditionService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    @Transactional
    public ParticipantAdditionRes addParticipants(long meetingId, long accessUserId, ParticipantAddReq participantAddReq) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        List<ParticipantDetail> participantDetails = createParticipantDetailByEmail(participantAddReq.emails());

        meeting.addParticipants(participantDetails, accessUserId);

        return new ParticipantAdditionRes(meeting.getId());
    }

    private List<ParticipantDetail> createParticipantDetailByEmail(List<String> participantEmails) {
        List<User> usersToAdd = userRepository.findByEmailIn(participantEmails);

        return usersToAdd.stream()
                .map(user -> new ParticipantDetail(user.getId(), MeetingRole.PARTICIPANT))
                .toList();
    }
}
