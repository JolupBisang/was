package com.jolupbisang.demo.application.meeting.service;

import com.jolupbisang.demo.application.meeting.exception.MeetingErrorCode;
import com.jolupbisang.demo.meeting.domain.model.*;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.ServiceLogicException;
import com.jolupbisang.demo.infrastructure.agenda.AgendaRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingCreationService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final AgendaRepository agendaRepository;

    @Transactional
    public Long createMeeting(MeetingReq meetingReq, Long userId) {
        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceLogicException(MeetingErrorCode.USER_NOT_FOUND));

        Meeting meeting = new Meeting(
                meetingReq.title(),
                meetingReq.location(),
                new ScheduledTime(meetingReq.scheduledStartTime(), meetingReq.scheduledStartTime().plusMinutes(meetingReq.targetTime())),
                new ActualProgressTime(),
                new RestTime(meetingReq.restInterval(), meetingReq.restDuration()),

                );
        meetingRepository.save(meeting);

        saveParticipants(meeting, leader, meetingReq.participants());
        saveAgendas(meeting, meetingReq.agendas());

        return meeting.getId();
    }

    private void saveParticipants(Meeting meeting, User leader, List<String> participantEmails) {
        meetingUserRepository.save(new MeetingUser(meeting, leader, true, ParticipantStatus.ACCEPTED));

        if (participantEmails != null && !participantEmails.isEmpty()) {
            participantEmails = participantEmails.stream().distinct().collect(Collectors.toList());
            List<User> participants = userRepository.findByEmailIn(participantEmails);
            List<MeetingUser> meetingUsers = participants.stream()
                    .map(participant -> new MeetingUser(meeting, participant, false, ParticipantStatus.ACCEPTED))
                    .toList();
            meetingUserRepository.saveAll(meetingUsers);
        }
    }

    private void saveAgendas(Meeting meeting, List<String> agendaContents) {
        if (agendaContents != null && !agendaContents.isEmpty()) {
            List<Agenda> agendas = agendaContents.stream()
                    .map(content -> new Agenda(meeting, content))
                    .toList();
            agendaRepository.saveAll(agendas);
        }
    }

}
