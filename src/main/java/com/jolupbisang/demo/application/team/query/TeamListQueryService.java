package com.jolupbisang.demo.application.team.query;

import com.jolupbisang.demo.application.team.query.dto.TeamListRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TeamListQueryService {

    private final TeamRepository teamRepository;
    private final MeetingRepository meetingRepository;

    @Transactional(readOnly = true)
    public TeamListRes getTeamsByUserId(String name, Long userId, Pageable pageable) {
        Slice<Team> teamSlice = teamRepository.findByNameContainingAndUserId(name, userId, pageable);
        LocalDateTime now = LocalDateTime.now();

        Slice<TeamListRes.TeamInfo> teamInfoSlice = teamSlice.map(team -> {
            Meeting closestMeeting = meetingRepository.findClosestMeetingByTeamId(team.getId(), now)
                    .orElse(null);
            return TeamListRes.TeamInfo.fromEntity(team, closestMeeting);
        });

        return TeamListRes.from(teamInfoSlice);
    }
}

