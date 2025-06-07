package com.jolupbisang.demo.application.agenda.service;

import com.jolupbisang.demo.application.agenda.dto.AgendaDetail;
import com.jolupbisang.demo.application.agenda.exception.AgendaErrorCode;
import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.common.MeetingSessionManager;
import com.jolupbisang.demo.application.event.AgendaChangedEvent;
import com.jolupbisang.demo.domain.agenda.Agenda;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.agenda.AgendaRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final MeetingRepository meetingRepository;

    private final MeetingAccessValidator meetingAccessValidator;
    private final ApplicationEventPublisher eventPublisher;
    private final MeetingSessionManager sessionManager;

    @Transactional
    public boolean changeAgendaStatus(Long agendaId, Long userId, boolean isCompleted) {
        Agenda agenda = agendaRepository.findByAgendaIdAndUserId(agendaId, userId)
                .orElseThrow(() -> new CustomException(AgendaErrorCode.UNAUTHORIZED));

        agenda.setIsCompleted(isCompleted);
        eventPublisher.publishEvent(new AgendaChangedEvent(agenda, agenda.getMeeting()));

        return agenda.getIsCompleted();
    }

    public List<AgendaDetail> findByMeetingId(Long meetingId, Long userId) {
        meetingAccessValidator.validateUserParticipating(meetingId, userId);

        List<Agenda> agendas = agendaRepository.findByMeetingId(meetingId);

        return agendas.stream()
                .map(AgendaDetail::fromEntity)
                .toList();
    }

    @Transactional
    public long addByMeetingId(Long meetingId, Long userId, String content) {
        meetingAccessValidator.validateUserIsHost(meetingId, userId);

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomException(AgendaErrorCode.MEETING_NOT_FOUND));

        if (meeting.isCompleted() || meeting.isCancelled()) {
            throw new CustomException(AgendaErrorCode.CANNOT_ADD_AGENDA);
        }

        Agenda agenda = new Agenda(meeting, content);
        agendaRepository.save(agenda);

        return agenda.getId();
    }

    @Transactional
    public long updateByAgendaId(Long agendaId, Long userId, String content) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new CustomException(AgendaErrorCode.NOT_FOUND));

        meetingAccessValidator.validateUserIsHost(agenda.getMeeting().getId(), userId);

        if (!agenda.getMeeting().isWaiting()) {
            throw new CustomException(AgendaErrorCode.CANNOT_UPDATE_AGENDA);
        }

        agenda.updateContent(content);

        return agenda.getId();
    }

    @Transactional
    public void deleteById(Long agendaId, Long userId) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new CustomException(AgendaErrorCode.NOT_FOUND));
        Meeting meeting = agenda.getMeeting();

        meetingAccessValidator.validateUserIsHost(meeting.getId(), userId);

        if (!meeting.isWaiting()) {
            throw new CustomException(AgendaErrorCode.CANNOT_DELETE_AGENDA);
        }

        agendaRepository.delete(agenda);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendToParticipants(AgendaChangedEvent event) {
        sessionManager.sendTextToParticipants(SocketResponseType.AGENDA_UPDATED, event.meeting().getId(), AgendaDetail.fromEntity(event.agenda()));
    }
}
