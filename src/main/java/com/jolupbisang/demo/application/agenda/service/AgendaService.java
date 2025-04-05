package com.jolupbisang.demo.application.agenda.service;

import com.jolupbisang.demo.application.agenda.exception.AgendaErrorCode;
import com.jolupbisang.demo.domain.agenda.Agenda;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.agenda.AgendaRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public void changeAgendaStatus(Long agendaId, Long userId, boolean isCompleted) {
        Agenda agenda = agendaRepository.findByAgendaIdAndUserId(agendaId, userId)
                .orElseThrow(() -> new CustomException(AgendaErrorCode.UNAUTHORIZED));

        agenda.setIsCompleted(isCompleted);
    }
}
