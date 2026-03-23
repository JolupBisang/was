package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.application.meeting.query.dto.AgendaInfoRes;
import com.jolupbisang.demo.application.meeting.query.dto.MeetingDetailRes;
import com.jolupbisang.demo.application.meeting.query.dto.ParticipantInfoRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meeting.model.Participant;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
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
    private final TeamRepository teamRepository;

    private static final String NOT_FOUND_USER_EMAIL = "알 수 없음";

    @Transactional(readOnly = true)
    public MeetingDetailRes getMeetingDetail(long meetingId, long accessUserId) {
        Meeting meeting = meetingRepository.findByIdWithAllDetail(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        meeting.validateViewAuthority(accessUserId);

        List<ParticipantInfoRes> participantInfos = getParticipantInfoRes(meeting);
        List<AgendaInfoRes> agendaInfoRes = getAgendaInfoRes(meeting);
        List<String> teamNames = getTeamNames(meeting);
        boolean isHost = meeting.isHost(accessUserId);

        return MeetingDetailRes.from(meeting, participantInfos, agendaInfoRes, teamNames, isHost);
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


    private List<AgendaInfoRes> getAgendaInfoRes(Meeting meeting) {
        return meeting.getAgendas().stream()
                .map(agenda -> new AgendaInfoRes(agenda.getId(), agenda.getContent(), agenda.getIsCompleted()))
                .collect(Collectors.toList());
    }

    private List<String> getTeamNames(Meeting meeting) {
        List<Long> teamIds = meeting.getTeamTags().stream()
                .map(teamTag -> teamTag.getTeamId())
                .toList();

        if (teamIds.isEmpty()) {
            return List.of();
        }

        List<Team> teams = teamRepository.findAllById(teamIds);
        return teams.stream()
                .map(team -> team.getTeamName().getName())
                .toList();
    }
}
