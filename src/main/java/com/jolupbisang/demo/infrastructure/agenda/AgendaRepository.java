package com.jolupbisang.demo.infrastructure.agenda;

import com.jolupbisang.demo.meeting.domain.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long>, AgendaRepositoryCustom {
    List<Agenda> findByMeetingId(Long meetingId);
}
