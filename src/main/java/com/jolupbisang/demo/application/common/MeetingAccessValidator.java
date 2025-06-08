package com.jolupbisang.demo.application.common;

import com.jolupbisang.demo.application.common.exception.MeetingAccessErrorCode;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.meeting.MeetingStatus;
import com.jolupbisang.demo.domain.meetingUser.MeetingUserStatus;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meetingUser.MeetingUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingAccessValidator {

    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;

    @Cacheable(value = "meetingInProgressAndUserParticipating", key = "{#meetingId, #userId}")
    public void validateMeetingInProgressAndUserParticipating(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingAccessErrorCode.NOT_FOUND));

        if (!meeting.getMeetingStatus().equals(MeetingStatus.IN_PROGRESS)) {
            throw new CustomException(MeetingAccessErrorCode.NOT_IN_PROGRESS);
        }

        validateUserParticipating(meetingId, userId);
    }

    @Cacheable(value = "userParticipating", key = "{#meetingId, #userId}")
    public void validateUserParticipating(Long meetingId, Long userId) {
        boolean isParticipant = meetingUserRepository.existsByMeetingIdAndUserIdAndStatusIn(meetingId, userId, MeetingUserStatus.ACCEPTED);

        if (!isParticipant) {
            throw new CustomException(MeetingAccessErrorCode.NOT_PARTICIPANT);
        }
    }

    @Cacheable(value = "userIsHost", key = "{#meetingId, #userId}")
    public void validateUserIsHost(Long meetingId, Long userId) {
        boolean isHost = meetingUserRepository.existsByMeetingIdAndUserIdAndIsHost(meetingId, userId, true);

        if (!isHost) {
            throw new CustomException(MeetingAccessErrorCode.NOT_LEADER);
        }
    }

    @Cacheable(value = "meetingIsInProgress", key = "#meetingId")
    public void validateMeetingIsInProgress(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingAccessErrorCode.NOT_FOUND));

        if (!meeting.isInProgress()) {
            throw new CustomException(MeetingAccessErrorCode.NOT_IN_PROGRESS);
        }
    }

    @Cacheable(value = "meetingIsWaiting", key = "#meetingId")
    public void validateMeetingIsWaiting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingAccessErrorCode.NOT_FOUND));

        if (!meeting.isWaiting()) {
            throw new CustomException(MeetingAccessErrorCode.NOT_WAITING);
        }
    }

    public void validateMeetingIsCompleted(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingAccessErrorCode.NOT_FOUND));

        if (!meeting.isCompleted()) {
            throw new CustomException(MeetingAccessErrorCode.NOT_COMPLETED);
        }
    }
}
