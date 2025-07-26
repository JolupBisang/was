package com.jolupbisang.demo.infrastructure.agenda;

import com.jolupbisang.demo.meeting.entity.Agenda;

import java.util.Optional;

public interface AgendaRepositoryCustom {

    Optional<Agenda> findByAgendaIdAndUserId(Long agendaId, Long userId);
}
