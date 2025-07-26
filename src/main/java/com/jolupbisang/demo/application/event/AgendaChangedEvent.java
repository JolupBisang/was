package com.jolupbisang.demo.application.event;

import com.jolupbisang.demo.meeting.domain.model.Agenda;
import com.jolupbisang.demo.meeting.domain.model.Meeting;

public record AgendaChangedEvent(
        Agenda agenda,
        Meeting meeting
) {
}
