package com.jolupbisang.demo.application.team.query.dto;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.team.model.Team;

import java.time.LocalDateTime;
import java.util.List;

public record TeamListRes(
        List<TeamInfo> teams
) {
    public static TeamListRes from(List<TeamInfo> teamInfos) {
        return new TeamListRes(teamInfos);
    }

    public record TeamInfo(
            Long teamId,
            String teamName,
            String meetingName,
            LocalDateTime scheduledStartTime,
            LocalDateTime scheduledEndTime
    ) {
        public static TeamInfo fromEntity(Team team, Meeting closestMeeting) {
            if (closestMeeting != null) {
                return new TeamInfo(
                        team.getId(),
                        team.getTeamName().getName(),
                        closestMeeting.getTitle(),
                        closestMeeting.getScheduledTime().getScheduledStartTime(),
                        closestMeeting.getScheduledTime().getScheduledEndTime()
                );
            }
            return new TeamInfo(
                    team.getId(),
                    team.getTeamName().getName(),
                    null,
                    null,
                    null
            );
        }
    }
}

