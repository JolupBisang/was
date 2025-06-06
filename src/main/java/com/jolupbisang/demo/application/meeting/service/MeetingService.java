package com.jolupbisang.demo.application.meeting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.common.MeetingSessionManager;
import com.jolupbisang.demo.application.event.MeetingCompletedEvent;
import com.jolupbisang.demo.application.event.MeetingStartingEvent;
import com.jolupbisang.demo.application.meeting.dto.MeetingDetailSummary;
import com.jolupbisang.demo.application.meeting.exception.MeetingErrorCode;
import com.jolupbisang.demo.domain.agenda.Agenda;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.meeting.MeetingStatus;
import com.jolupbisang.demo.domain.meetingUser.MeetingUser;
import com.jolupbisang.demo.domain.meetingUser.MeetingUserStatus;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.agenda.AgendaRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meetingUser.MeetingUserRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingReq;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingUpdateReq;
import com.jolupbisang.demo.presentation.meeting.dto.response.MeetingDetailRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final AgendaRepository agendaRepository;
    private final MeetingSessionManager meetingSessionManager;
    private final MeetingAccessValidator meetingAccessValidator;

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createMeeting(MeetingReq meetingReq, Long userId) {
        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.USER_NOT_FOUND));

        Meeting meeting = meetingReq.toEntity();
        meetingRepository.save(meeting);

        saveParticipants(meeting, leader, meetingReq.participants());
        saveAgendas(meeting, meetingReq.agendas());

        return meeting.getId();
    }

    @Transactional(readOnly = true)
    public MeetingDetailRes getMeetingDetail(Long meetingId, Long userId) {
        meetingAccessValidator.validateUserParticipating(meetingId, userId);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        List<User> participants = userRepository.findByMeetingId(meetingId);
        boolean isHost = meetingUserRepository.existsByMeetingIdAndUserIdAndIsHost(meetingId, userId, true);

        return MeetingDetailRes.fromEntity(meeting, participants, isHost);
    }

    @Transactional(readOnly = true)
    public List<MeetingDetailSummary> getMeetingsByYearAndMonth(int year, int month, Long userId) {
        if (year < 0 || month < 1 || month > 12) {
            throw new CustomException(MeetingErrorCode.INVALID_DATE);
        }

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        List<Meeting> meetings = meetingRepository.findByUserIdAndStartTimeBetween(userId, startOfMonth, endOfMonth);

        return meetings.stream()
                .map(MeetingDetailSummary::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeMeetingStatus(Long meetingId, Long userId, String apiTargetStatus) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        switch (apiTargetStatus) {
            case "IN_PROGRESS":
                startMeeting(meeting, meetingId, userId);
                break;
            case "COMPLETED":
                completeMeeting(meeting, meetingId, userId);
                break;
            case "CANCELLED":
                cancelMeeting(meeting, meetingId, userId);
                break;
            default:
                throw new CustomException(MeetingErrorCode.CANNOT_CHANGE_TO_REQUESTED_STATUS);
        }
    }

    @Transactional
    public void updateMeeting(Long meetingId, Long userId, MeetingUpdateReq meetingUpdateReq) {
        meetingAccessValidator.validateUserIsHost(meetingId, userId);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        if (!meeting.isWaiting()) {
            throw new CustomException(MeetingErrorCode.CANNOT_UPDATE_MEETING);
        }

        meeting.updateMeetingDetails(
                meetingUpdateReq.title(),
                meetingUpdateReq.location(),
                meetingUpdateReq.scheduledStartTime(),
                meetingUpdateReq.targetTime(),
                meetingUpdateReq.restInterval(),
                meetingUpdateReq.restDuration()
        );
    }

    public LocalDateTime getMeetingStartTime(long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));
        return meeting.getActualStartTime();
    }

    private void saveParticipants(Meeting meeting, User leader, List<String> participantEmails) {
        meetingUserRepository.save(new MeetingUser(meeting, leader, true, MeetingUserStatus.ACCEPTED));

        if (participantEmails != null && !participantEmails.isEmpty()) {
            participantEmails = participantEmails.stream().distinct().collect(Collectors.toList());
            List<User> participants = userRepository.findByEmailIn(participantEmails);
            List<MeetingUser> meetingUsers = participants.stream()
                    .map(participant -> new MeetingUser(meeting, participant, false, MeetingUserStatus.ACCEPTED))
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

    private void startMeeting(Meeting meeting, Long meetingId, Long userId) {
        meetingAccessValidator.validateUserIsHost(meetingId, userId);
        meetingAccessValidator.validateMeetingIsWaiting(meetingId);
        meeting.startMeeting();
        eventPublisher.publishEvent(new MeetingStartingEvent(this, meetingId));
    }

    private void completeMeeting(Meeting meeting, Long meetingId, Long userId) {
        meetingAccessValidator.validateUserIsHost(meetingId, userId);
        meetingAccessValidator.validateMeetingIsInProgress(meetingId);

        meeting.endMeeting();
        meetingSessionManager.sendTextToParticipants(SocketResponseType.MEETING_COMPLETED, meetingId, "회의가 종료되었습니다.");

        eventPublisher.publishEvent(new MeetingCompletedEvent(this, meetingId));
    }

    private void cancelMeeting(Meeting meeting, Long meetingId, Long userId) {
        meetingAccessValidator.validateUserIsHost(meetingId, userId);
        if (meeting.getMeetingStatus() != MeetingStatus.WAITING) {
            throw new CustomException(MeetingErrorCode.MEETING_NOT_WAITING);
        }
        meeting.cancelMeeting();
    }
}
