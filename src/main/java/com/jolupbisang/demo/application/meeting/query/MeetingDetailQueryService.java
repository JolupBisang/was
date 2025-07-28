package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.application.meeting.exception.MeetingNotFoundException;
import com.jolupbisang.demo.application.meeting.exception.NotParticipantException;
import com.jolupbisang.demo.application.meeting.query.dto.MeetingDetailRes;
import com.jolupbisang.demo.application.meeting.query.dto.ParticipantInfoRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.Participant;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingDetailQueryService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    private static final String NOT_FOUND_USER_EMAIL = "알 수 없음";

    @Transactional(readOnly = true)
    public MeetingDetailRes getMeetingDetail(long meetingId, long accessUserId) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(Map.of("meetingId", meetingId)));

        if (!meeting.isParticipant(accessUserId)) {
            throw new NotParticipantException();
        }

        List<ParticipantInfoRes> participantInfos = getParticipantInfoRes(meeting);
        boolean isHost = meeting.isHost(accessUserId);

        return MeetingDetailRes.from(meeting, participantInfos, isHost);
    }

    private List<ParticipantInfoRes> getParticipantInfoRes(Meeting meeting) {
        List<Long> userIds = getUserIds(meeting);

        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        return meeting.getParticipants().stream()
                .map(p -> createParticipantInfoRes(p, userMap))
                .collect(Collectors.toList());
    }

    private List<Long> getUserIds(Meeting meeting) {
        return meeting.getParticipants().stream()
                .map(Participant::getUserId)
                .collect(Collectors.toList());
    }

    private ParticipantInfoRes createParticipantInfoRes(Participant participant, Map<Long, User> userMap) {
        User user = userMap.get(participant.getUserId());
        String email = (user != null) ? user.getEmail() : NOT_FOUND_USER_EMAIL;
        return new ParticipantInfoRes(participant.getUserId(), email, participant.getRole());
    }
}
