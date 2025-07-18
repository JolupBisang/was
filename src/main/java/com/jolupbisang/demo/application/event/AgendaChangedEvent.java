package com.jolupbisang.demo.application.event;

import com.jolupbisang.demo.domain.agenda.Agenda;
import com.jolupbisang.demo.domain.meeting.entity.Meeting;

public record AgendaChangedEvent(
        Agenda agenda,
        Meeting meeting
) {
}
