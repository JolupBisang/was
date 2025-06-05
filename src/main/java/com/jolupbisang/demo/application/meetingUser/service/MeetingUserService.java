package com.jolupbisang.demo.application.meetingUser.service;

import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.meetingUser.exception.MeetingUserErrorCode;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.meetingUser.MeetingUser;
import com.jolupbisang.demo.domain.meetingUser.MeetingUserStatus;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meetingUser.MeetingUserRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingUserService {

    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final UserRepository userRepository;
    private final MeetingAccessValidator meetingAccessValidator;

    @Transactional
    public void addParticipants(Long meetingId, Long hostUserId, List<String> participantEmails) {
        meetingAccessValidator.validateUserIsHost(meetingId, hostUserId);
        
        Meeting meeting = validateMeetingForAdd(meetingId);
        
        Set<String> existingEmails = meetingUserRepository.findParticipantsByMeetingId(meetingId)
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        List<String> newEmails = participantEmails.stream()
                .filter(email -> !existingEmails.contains(email))
                .toList();

        if (newEmails.isEmpty()) {
            return;
        }

        List<MeetingUser> newMeetingUsers = userRepository.findByEmailIn(newEmails)
                .stream()
                .map(user -> new MeetingUser(meeting, user, false, MeetingUserStatus.ACCEPTED))
                .toList();

        meetingUserRepository.saveAll(newMeetingUsers);
    }

    @Transactional
    public void removeParticipant(Long meetingId, Long hostUserId, Long participantUserId) {
        meetingAccessValidator.validateUserIsHost(meetingId, hostUserId);
        
        validateMeetingForRemove(meetingId);

        MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, participantUserId)
                .orElseThrow(() -> new CustomException(MeetingUserErrorCode.USER_NOT_PARTICIPANT));

        if (meetingUser.isHost()) {
            throw new CustomException(MeetingUserErrorCode.CANNOT_REMOVE_HOST);
        }

        meetingUserRepository.delete(meetingUser);
    }

    private Meeting validateMeetingForAdd(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingUserErrorCode.MEETING_NOT_FOUND));
        
        if (meeting.isCompleted() || meeting.isCancelled()) {
            throw new CustomException(MeetingUserErrorCode.CANNOT_ADD_TO_COMPLETED_MEETING);
        }
        
        return meeting;
    }

    private void validateMeetingForRemove(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingUserErrorCode.MEETING_NOT_FOUND));
        
        if (!meeting.isWaiting()) {
            throw new CustomException(MeetingUserErrorCode.CANNOT_REMOVE_FROM_NON_WAITING_MEETING);
        }
    }
} 
