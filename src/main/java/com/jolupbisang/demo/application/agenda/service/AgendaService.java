package com.jolupbisang.demo.application.agenda.service;

import com.jolupbisang.demo.application.agenda.dto.AgendaDetail;
import com.jolupbisang.demo.application.agenda.exception.AgendaErrorCode;
import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.domain.agenda.Agenda;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.agenda.AgendaRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingAccessValidator meetingAccessValidator;

    @Transactional
    public boolean changeAgendaStatus(Long agendaId, Long userId, boolean isCompleted) {
        Agenda agenda = agendaRepository.findByAgendaIdAndUserId(agendaId, userId)
                .orElseThrow(() -> new CustomException(AgendaErrorCode.UNAUTHORIZED));

        agenda.setIsCompleted(isCompleted);

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
    public long addAgenda(Long meetingId, Long userId, String content) {
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
    public void deleteAgenda(Long agendaId, Long userId) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new CustomException(AgendaErrorCode.NOT_FOUND));
        Meeting meeting = agenda.getMeeting();

        meetingAccessValidator.validateUserIsHost(meeting.getId(), userId);

        if (!meeting.isWaiting()) {
            throw new CustomException(AgendaErrorCode.CANNOT_DELETE_AGENDA);
        }

        agendaRepository.delete(agenda);
    }
}
