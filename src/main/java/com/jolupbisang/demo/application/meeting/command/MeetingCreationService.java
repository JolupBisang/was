package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.MeetingCreationReq;
import com.jolupbisang.demo.application.meeting.command.dto.MeetingCreationRes;
import com.jolupbisang.demo.domain.meeting.model.*;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingCreationService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    @Transactional
    public MeetingCreationRes create(MeetingCreationReq meetingCreationReq, long hostId) {
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new NotFoundException("hostId: %d", hostId));

        List<User> participants = userRepository.findByEmailIn(meetingCreationReq.participants());
        Meeting meeting = createMeeting(host, participants, meetingCreationReq);
        meetingRepository.save(meeting);

        return MeetingCreationRes.of(meeting.getId());

    }

    private Meeting createMeeting(User host, List<User> invitedUsers, MeetingCreationReq req) {
        ScheduledTime scheduledTime = createScheduledTime(req);
        ActualProgressTime actualProgressTime = new ActualProgressTime();
        RestTime restTime = createRestTime(req);
        List<ParticipantDetail> participantDetails = createParticipantDetails(host, invitedUsers);
        List<AgendaDetail> agendaDetails = createAgendaDetails(req.agendas());

        return new Meeting(
                req.title(),
                req.location(),
                scheduledTime,
                actualProgressTime,
                restTime,
                participantDetails,
                agendaDetails
        );
    }

    private ScheduledTime createScheduledTime(MeetingCreationReq req) {
        return new ScheduledTime(
                req.scheduledStartTime(),
                req.scheduledStartTime().plusMinutes(req.targetTime())
        );
    }

    private RestTime createRestTime(MeetingCreationReq req) {
        return new RestTime(req.restInterval(), req.restDuration());
    }

    private List<ParticipantDetail> createParticipantDetails(User host, List<User> invitedUsers) {
        List<ParticipantDetail> participantDetails = new ArrayList<>();
        participantDetails.add(new ParticipantDetail(host.getId(), MeetingRole.HOST));
        invitedUsers.stream()
                .filter(user -> !user.getId().equals(host.getId()))
                .map(user -> new ParticipantDetail(user.getId(), MeetingRole.PARTICIPANT))
                .forEach(participantDetails::add);

        return participantDetails;
    }

    private List<AgendaDetail> createAgendaDetails(List<String> agendaContents) {
        return agendaContents.stream()
                .map(AgendaDetail::new)
                .collect(Collectors.toList());
    }
}
