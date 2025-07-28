package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import com.jolupbisang.demo.domain.meeting.exception.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 회의 관리 어그리거트 루트
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    private List<Agenda> agendas = new ArrayList<>();

    private static final long MAX_MEETING_HOST_COUNT = 1L;

    public Meeting(String title, String location, ScheduledTime scheduledTime, ActualProgressTime actualProgressTime, RestTime restTime, List<ParticipantDetail> participantDetails, List<AgendaDetail> agendaDetails) {
        setTitle(title);
        setLocation(location);
        setScheduledTime(scheduledTime);
        setActualProgressTime(actualProgressTime);
        setRestTime(restTime);
        addParticipants(participantDetails);
        addAgendas(agendaDetails);
        initiateStatus();
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

    public void addParticipants(List<ParticipantDetail> participantDetails) {
        if (participantDetails == null) {
            throw new NullParticipantsException();
        }

        List<Participant> originalParticipants = new ArrayList<>(participants);

        try {
            for (ParticipantDetail detail : participantDetails) {
                participants.removeIf(p -> p.getUserId().equals(detail.getUserId()));
                participants.add(new Participant(this, detail.getUserId(), detail.getMeetingRole()));
            }

            validateHostCount();
        } catch (TooManyHostException e) {
            participants.clear();
            participants.addAll(originalParticipants);
            throw e;
        }
    }

    public void addAgendas(List<AgendaDetail> agendaDetails) {
        if (agendaDetails == null) {
            throw new NullAgendaException();
        }
        for (AgendaDetail detail : agendaDetails) {
            Agenda newAgenda = new Agenda(this, detail.getContent());
            this.agendas.add(newAgenda);
        }
    }

    public void validateViewAuthority(long accessUserId) {
        if (!isParticipant(accessUserId)) {
            throw new NotParticipantException();
        }
    }

    public boolean isHost(long userId) {
        return participants.stream()
                .anyMatch(p -> p.getUserId().equals(userId) && p.getRole() == MeetingRole.HOST);
    }

    public boolean isParticipant(long userId) {
        return participants.stream()
                .anyMatch(p -> p.getUserId().equals(userId));
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

    private void initiateStatus() {
        meetingStatus = MeetingStatus.WAITING;
    }

    private void validateHostCount() {
        long hostCount = participants.stream()
                .filter(p -> p.getRole() == MeetingRole.HOST)
                .count();

        if (hostCount > MAX_MEETING_HOST_COUNT) {
            throw new TooManyHostException();
        }
    }
}
