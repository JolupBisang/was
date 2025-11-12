package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.application.meeting.command.dto.TeamTagRemovalReq;
import com.jolupbisang.demo.application.meeting.command.dto.TeamTagRemovalRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.team.model.Team;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamTagRemovalService {

    private final MeetingRepository meetingRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public TeamTagRemovalRes removeTeamTag(Long meetingId, Long accessUserId, TeamTagRemovalReq request) {
        Meeting meeting = meetingRepository.findByIdWithTeamTagsAndParticipants(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        meeting.validateHostAuthority(accessUserId);

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new NotFoundException("teamId: %d", request.teamId()));

        team.validateViewAuthority(accessUserId);

        meeting.removeTeamTag(request.teamId());

        return new TeamTagRemovalRes(meeting.getId());
    }
}

