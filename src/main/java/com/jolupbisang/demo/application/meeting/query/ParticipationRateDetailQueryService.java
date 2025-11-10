package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.application.meeting.exception.MeetingApplicationErrorCode;
import com.jolupbisang.demo.application.meeting.query.dto.ParticipationRateRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ParticipationRateDetailQueryService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    public ParticipationRateRes getParticipationRateByMeeting(long meetingId, long accessUserId) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        if (!meeting.isParticipant(accessUserId)) {
            throw new ApplicationException(MeetingApplicationErrorCode.NOT_PARTICIPANT, "meetingId: %d, userId: %d", meetingId, accessUserId);
        }

        Map<Long, Double> participantRates = getParticipantRates(meeting);
        Map<Long, String> nicknames = getNicknames(participantRates);

        return ParticipationRateRes.of(participantRates, nicknames);
    }

    private Map<Long, String> getNicknames(Map<Long, Double> participantRates) {
        Map<Long, String> nicknames = new HashMap<>();
        userRepository.findIdAndNicknameByIdIn(participantRates.keySet().stream().toList())
                .forEach(summary -> nicknames.put(summary.id(), summary.nickname()));
        return nicknames;
    }

    private static Map<Long, Double> getParticipantRates(Meeting meeting) {
        Map<Long, Double> participantRates = new HashMap<>();
        meeting.getParticipants().stream().forEach(participant -> {
            long userId = participant.getUserId();
            double rate = participant.getParticipationRate().getRate();
            participantRates.put(userId, rate);
        });
        return participantRates;
    }
}
