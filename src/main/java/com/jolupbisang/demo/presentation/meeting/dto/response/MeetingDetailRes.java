package com.jolupbisang.demo.presentation.meeting.dto.response;

import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record MeetingDetailRes(
        Long meetingId,
        String title,
        String location,
        LocalDateTime scheduledStartTime,
        Integer targetTime,
        Integer restInterval,
        Integer restDuration,
        String meetingStatus,
        List<Participant> participants,
        boolean isHost
) {

    public static MeetingDetailRes fromEntity(Meeting meeting, List<User> participants, boolean isHost) {
        return new MeetingDetailRes(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getLocation(),
                meeting.getScheduledStartTime(),
                meeting.getTargetTime(),
                meeting.getRestInterval(),
                meeting.getRestDuration(),
                meeting.getMeetingStatus().name(),
                participants.stream().map(Participant::fromEntity).collect(Collectors.toList()),
                isHost
        );
    }

    public record Participant(
            Long userId,
            String email
    ) {
        public static Participant fromEntity(User user) {
            return new Participant(user.getId(), user.getEmail());
        }
    }
}
