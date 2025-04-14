package com.jolupbisang.demo.application.common.validator;

import com.jolupbisang.demo.application.common.exception.MeetingAccessErrorCode;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.meeting.MeetingStatus;
import com.jolupbisang.demo.domain.meetingUser.MeetingUserStatus;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meetingUser.MeetingUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingAccessValidator {

    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;

    public void validateMeetingInProgressAndUserParticipating(Long meetingId, Long userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingAccessErrorCode.NOT_FOUND));

        if (!meeting.getMeetingStatus().equals(MeetingStatus.IN_PROGRESS)) {
            throw new CustomException(MeetingAccessErrorCode.NOT_IN_PROGRESS);
        }

        boolean isParticipant = meetingUserRepository.existsByMeetingIdAndUserIdAndStatusIn(meetingId, userId, MeetingUserStatus.ACCEPTED);

        if (!isParticipant) {
            throw new CustomException(MeetingAccessErrorCode.NOT_PARTICIPANT);
        }
    }

    public void validateUserParticipating(Long meetingId, Long userId) {
        boolean isParticipant = meetingUserRepository.existsByMeetingIdAndUserIdAndStatusIn(meetingId, userId, MeetingUserStatus.ACCEPTED);

        if (!isParticipant) {
            throw new CustomException(MeetingAccessErrorCode.NOT_PARTICIPANT);
        }
    }
}
