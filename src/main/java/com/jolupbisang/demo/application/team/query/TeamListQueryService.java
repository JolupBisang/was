package com.jolupbisang.demo.application.team.query;

import com.jolupbisang.demo.application.team.query.dto.TeamListRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamListQueryService {

    private final TeamRepository teamRepository;
    private final MeetingRepository meetingRepository;

    @Transactional(readOnly = true)
    public List<TeamListRes> getTeamsByUserId(Long userId) {
        List<Team> teams = teamRepository.findByMembersUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        
        return teams.stream()
                .map(team -> {
                    Meeting closestMeeting = meetingRepository.findClosestMeetingByTeamId(team.getId(), now)
                            .orElse(null);
                    return TeamListRes.fromEntity(team, closestMeeting);
                })
                .toList();
    }
}

