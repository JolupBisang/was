package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import com.jolupbisang.demo.domain.meeting.dto.CreatedAgendaDetail;
import com.jolupbisang.demo.domain.meeting.dto.MeetingDetailUpdateDto;
import com.jolupbisang.demo.domain.meeting.event.MeetingCompletedEvent;
import com.jolupbisang.demo.domain.meeting.event.MeetingStartedEvent;
import com.jolupbisang.demo.domain.meeting.exception.*;
import com.jolupbisang.demo.global.event.Events;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        setParticipants(participantDetails);
        setAgendas(agendaDetails);
        initiateStatus();
    }

    public void start(long accessUserId) {
        validateHostAuthority(accessUserId);

        if (!isWaiting()) {
            throw new MeetingNotWaitingStatusException();
        }

        meetingStatus = MeetingStatus.IN_PROGRESS;
        actualProgressTime = new ActualProgressTime(LocalDateTime.now(), null);
        Events.raise(new MeetingStartedEvent(id));
    }

    public void complete(long accessUserId) {
        validateHostAuthority(accessUserId);

        if (!isInProgress()) {
            throw new NotProgressingStatusException();
        }
        meetingStatus = MeetingStatus.COMPLETED;
        actualProgressTime = new ActualProgressTime(actualProgressTime.getActualStartTime(), LocalDateTime.now());
        Events.raise(new MeetingCompletedEvent(id));
    }

    public void cancel(long accessUserId) {
        validateHostAuthority(accessUserId);

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

    public void addParticipants(List<ParticipantDetail> participantDetails, long accessUserId) {
        validateHostAuthority(accessUserId);

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

    public void removeParticipant(long accessUserId, long participantId) {
        validateHostAuthority(accessUserId);

        participants.removeIf(p -> p.getUserId().equals(participantId));
    }

    public List<CreatedAgendaDetail> addAgendas(List<AgendaDetail> agendaDetails) {
        if (agendaDetails == null) {
            throw new NullAgendaException();
        }

        List<CreatedAgendaDetail> createdAgendaDetails = new ArrayList<>();
        for (AgendaDetail detail : agendaDetails) {
            Agenda newAgenda = new Agenda(this, detail.getContent());
            this.agendas.add(newAgenda);
            createdAgendaDetails.add(new CreatedAgendaDetail(newAgenda.getId(), newAgenda.getContent()));
        }

        return createdAgendaDetails;
    }

    public void changeAgendaStatus(long agendaId, long accessUserId, boolean isCompleted) {
        validateHostAuthority(accessUserId);

        Agenda foundAgenda = agendas.stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new AgendaNotExistingException(Map.of("agendaId", agendaId)));

        foundAgenda.changeStatus(isCompleted);
    }

    public void deleteAgenda(long agendaId, long accessUserId) {
        validateHostAuthority(accessUserId);

        Agenda foundAgenda = agendas.stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new AgendaNotExistingException(Map.of("agendaId", agendaId)));

        agendas.remove(foundAgenda);
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

    public void updateDetails(MeetingDetailUpdateDto updateDto, long accessUserId) {
        validateHostAuthority(accessUserId);

        if (!isWaiting()) {
            throw new MeetingNotWaitingStatusException();
        }

        setTitle(updateDto.title());
        setLocation(updateDto.location());
        setScheduledTime(updateDto.scheduledTime());
        setRestTime(updateDto.RestTime());
    }

    public void updateParticipationRates(Map<Long, Double> participationRates, Map<Long, Long> participantChunks) {
        participants
                .forEach(participant -> {
                    if (participationRates.containsKey(participant.getUserId())) {
                        participant.updateParticipationRate(participationRates.get(participant.getUserId()), participantChunks.get(participant.getUserId()));
                    }
                });
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

    private void setParticipants(List<ParticipantDetail> participantDetails) {
        participantDetails.stream()
                .map(detail -> new Participant(this, detail.getUserId(), detail.getMeetingRole()))
                .forEach(participants::add);
    }

    private void setAgendas(List<AgendaDetail> agendaDetails) {
        agendaDetails.stream()
                .map(agendaDetail -> new Agenda(this, agendaDetail.getContent()))
                .forEach(agendas::add);
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

    private void validateHostAuthority(long accessUserId) {
        boolean isHost = participants.stream()
                .anyMatch(p -> p.getUserId().equals(accessUserId) && p.getRole() == MeetingRole.HOST);

        if (!isHost) {
            throw new NotHostException();
        }
    }
}
