package com.jolupbisang.demo.infrastructure.agenda;

import com.jolupbisang.demo.domain.agenda.Agenda;

import java.util.Optional;

public interface AgendaRepositoryCustom {

    Optional<Agenda> findByAgendaIdAndUserId(Long agendaId, Long userId);
}
