package com.jolupbisang.demo.application.agenda.service;

import com.jolupbisang.demo.application.agenda.dto.AgendaDetail;
import com.jolupbisang.demo.application.agenda.exception.AgendaErrorCode;
import com.jolupbisang.demo.application.common.validator.MeetingAccessValidator;
import com.jolupbisang.demo.domain.agenda.Agenda;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.agenda.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaService {
    private final AgendaRepository agendaRepository;
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
}
