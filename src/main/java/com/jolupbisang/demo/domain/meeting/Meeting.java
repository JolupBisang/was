package com.jolupbisang.demo.domain.meeting;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import com.jolupbisang.demo.domain.meeting.exception.MeetingNotWaitingStatusException;
import com.jolupbisang.demo.domain.meeting.exception.NotProgressingStatusException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    public Meeting(String title, String location, LocalDateTime scheduledStartTime, int targetTime, int restInterval, int restDuration) {
        this.title = title;
        this.location = location;
        this.scheduledTime = new ScheduledTime(scheduledStartTime, scheduledStartTime.plusMinutes(targetTime));
        this.restTime = new RestTime(restInterval, restDuration);
        this.meetingStatus = MeetingStatus.WAITING;
    }

    public void start() {
        if (!isWaiting()) {
            throw new MeetingNotWaitingStatusException();
        }
        this.meetingStatus = MeetingStatus.IN_PROGRESS;
        this.actualProgressTime = new ActualProgressTime(LocalDateTime.now(), null);
    }

    public void end() {
        if (!isInProgress()) {
            throw new NotProgressingStatusException();
        }
        this.meetingStatus = MeetingStatus.COMPLETED;
        this.actualProgressTime = new ActualProgressTime(this.actualProgressTime.getActualStartTime(), LocalDateTime.now());
    }

    public void cancel() {
        if (!isWaiting()) {
            throw new MeetingNotWaitingStatusException();
        }
        this.meetingStatus = MeetingStatus.CANCELLED;
    }

    public boolean isWaiting() {
        return this.meetingStatus == MeetingStatus.WAITING;
    }

    public boolean isInProgress() {
        return this.meetingStatus == MeetingStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this.meetingStatus == MeetingStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return this.meetingStatus == MeetingStatus.CANCELLED;
    }

    public void updateMeetingDetails(String title, String location, LocalDateTime scheduledStartTime, int targetTime, int restInterval, int restDuration) {
        this.title = title;
        this.location = location;
        this.scheduledTime = new ScheduledTime(scheduledStartTime, scheduledStartTime.plusMinutes(targetTime));
        this.restTime = new RestTime(restInterval, restDuration);
    }
}
