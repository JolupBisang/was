package com.jolupbisang.demo.application.team.query.dto;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.team.model.Team;

import java.time.LocalDateTime;

public record TeamListRes(
        Long teamId,
        String teamName,
        String meetingName,
        LocalDateTime scheduledStartTime,
        LocalDateTime scheduledEndTime
) {
    public static TeamListRes fromEntity(Team team, Meeting closestMeeting) {
        if (closestMeeting != null) {
            return new TeamListRes(
                    team.getId(),
                    team.getTeamName().getName(),
                    closestMeeting.getTitle(),
                    closestMeeting.getScheduledTime().getScheduledStartTime(),
                    closestMeeting.getScheduledTime().getScheduledEndTime()
            );
        }
        return new TeamListRes(
                team.getId(),
                team.getTeamName().getName(),
                null,
                null,
                null
        );
    }
}

