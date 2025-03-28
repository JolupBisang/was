package com.jolupbisang.demo.application.meeting.service;

import com.jolupbisang.demo.application.meeting.dto.MeetingReq;
import com.jolupbisang.demo.application.meeting.exception.MeetingErrorCode;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;

    @Transactional
    public void createMeeting(MeetingReq meetingReq, Long userId) {
        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.NOT_FOUND));

        Meeting meeting = meetingReq.toEntity();
        meetingRepository.save(meeting);

        List<User> participants = userRepository.findByEmailIn(meetingReq.participants());
        meetingUserRepository.save(new MeetingUser(meeting, leader, true, MeetingUserStatus.ACCEPTED));
        meetingUserRepository.saveAll(participants.stream().map(p ->
                new MeetingUser(meeting, p, false, MeetingUserStatus.WAITING)).toList());
    }
}
