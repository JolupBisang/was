package com.jolupbisang.demo.application.meeting.query.dto;

import com.jolupbisang.demo.domain.meeting.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingDetailRes(
        Long meetingId,
        String title,
        String location,
        LocalDateTime scheduledStartTime,
        Integer targetTime,
        Integer restInterval,
        Integer restDuration,
        String meetingStatus,
        List<ParticipantInfoRes> participants,
        List<AgendaInfoRes> agendas,
        boolean isHost
) {

    public static MeetingDetailRes from(Meeting meeting, List<ParticipantInfoRes> participantInfos, List<AgendaInfoRes> agendas, boolean isHost) {
        return new MeetingDetailRes(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getLocation(),
                meeting.getScheduledTime().getScheduledStartTime(),
                meeting.getScheduledTime().getTargetTime(),
                meeting.getRestTime().getRestInterval(),
                meeting.getRestTime().getRestDuration(),
                meeting.getMeetingStatus().name(),
                participantInfos,
                agendas,
                isHost
        );
    }
}
