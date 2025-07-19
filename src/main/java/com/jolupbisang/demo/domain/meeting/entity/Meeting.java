package com.jolupbisang.demo.domain.meeting.entity;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import com.jolupbisang.demo.domain.meeting.exception.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String location;

    @Embedded
    private ScheduledTime scheduledTime;

    @Embedded
    private ActualProgressTime actualProgressTime;

    @Embedded
    private RestTime restTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus meetingStatus;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "meeting_participants",
            joinColumns = @JoinColumn(name = "meeting_id"))
    private Set<Participant> participants;

    public Meeting(String title, String location, ScheduledTime scheduledTime, ActualProgressTime actualProgressTime, RestTime restTime, Set<Participant> participants) {
        setTitle(title);
        setLocation(location);
        setScheduledTime(scheduledTime);
        setActualProgressTime(actualProgressTime);
        setRestTime(restTime);
        setParticipants(participants);
        meetingStatus = MeetingStatus.WAITING;
    }

    public void start() {
        if (!isWaiting()) {
            throw new MeetingNotWaitingStatusException();
        }
        meetingStatus = MeetingStatus.IN_PROGRESS;
        actualProgressTime = new ActualProgressTime(LocalDateTime.now(), null);
    }

    public void end() {
        if (!isInProgress()) {
            throw new NotProgressingStatusException();
        }
        meetingStatus = MeetingStatus.COMPLETED;
        actualProgressTime = new ActualProgressTime(actualProgressTime.getActualStartTime(), LocalDateTime.now());
    }

    public void cancel() {
        if (!isWaiting()) {
            throw new MeetingNotWaitingStatusException();
        }
        meetingStatus = MeetingStatus.CANCELLED;
    }

    public boolean isWaiting() {
        return meetingStatus == MeetingStatus.WAITING;
    }

    public boolean isInProgress() {
        return meetingStatus == MeetingStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return meetingStatus == MeetingStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return meetingStatus == MeetingStatus.CANCELLED;
    }

    public void updateMeetingDetails(String title, String location, LocalDateTime scheduledStartTime, int targetTime, int restInterval, int restDuration) {
        this.title = title;
        this.location = location;
        this.scheduledTime = new ScheduledTime(scheduledStartTime, scheduledStartTime.plusMinutes(targetTime));
        this.restTime = new RestTime(restInterval, restDuration);
    }

    private void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new EmptyTitleException();
        }
        this.title = title;
    }

    private void setLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new EmptyLocationException();
        }
        this.location = location;
    }

    private void setScheduledTime(ScheduledTime scheduledTime) {
        if (scheduledTime == null) {
            throw new NullScheduledTimeException();
        }
        this.scheduledTime = scheduledTime;
    }

    private void setActualProgressTime(ActualProgressTime actualProgressTime) {
        if (actualProgressTime == null) {
            throw new NullActualProgressTimeException();
        }
        this.actualProgressTime = actualProgressTime;
    }

    private void setRestTime(RestTime restTime) {
        if (restTime == null) {
            throw new NullRestTimeException();
        }
        this.restTime = restTime;
    }

    private void setParticipants(Set<Participant> participants) {
        if (participants == null) {
            throw new NullParticipantsException();
        }
        this.participants = participants;
    }
}
