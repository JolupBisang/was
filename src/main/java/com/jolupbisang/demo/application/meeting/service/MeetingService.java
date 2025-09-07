package com.jolupbisang.demo.application.meeting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.common.MeetingSessionManager;
import com.jolupbisang.demo.application.meeting.dto.MeetingDetailSummary;
import com.jolupbisang.demo.application.meeting.exception.MeetingErrorCode;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.agenda.AgendaRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meetingUser.MeetingUserRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
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


    public LocalDateTime getMeetingStartTime(long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));
        return meeting.getScheduledTime().getScheduledStartTime();
    }
}
