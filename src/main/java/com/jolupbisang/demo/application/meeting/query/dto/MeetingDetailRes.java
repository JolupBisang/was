package com.jolupbisang.demo.application.meeting.query.dto;

import com.jolupbisang.demo.domain.meeting.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingDetailRes(
        Long meetingId,
        String title,
        String location,
        LocalDateTime scheduledStartTime,
        LocalDateTime actualStartTime,
        LocalDateTime scheduledEndTime,
        Integer targetTime,
        Integer restInterval,
        Integer restDuration,
        String meetingStatus,
        List<ParticipantInfoRes> participants,
        List<AgendaInfoRes> agendas,
        List<String> teamNames,
        boolean isHost
) {

    public static MeetingDetailRes from(Meeting meeting, List<ParticipantInfoRes> participantInfos, List<AgendaInfoRes> agendas, List<String> teamNames, boolean isHost) {
        return new MeetingDetailRes(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getLocation(),
                meeting.getScheduledTime().getScheduledStartTime(),
                meeting.getActualProgressTime() != null ? meeting.getActualProgressTime().getActualStartTime() : null,
                meeting.getActualProgressTime() != null ? meeting.getScheduledTime().getScheduledStartTime() : null,
                meeting.getScheduledTime().getTargetTime(),
                meeting.getRestTime().getRestInterval(),
                meeting.getRestTime().getRestDuration(),
                meeting.getMeetingStatus().name(),
                participantInfos,
                agendas,
                teamNames,
                isHost
        );
    }
}
