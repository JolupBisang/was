package com.jolupbisang.demo.application.event;

import com.jolupbisang.demo.domain.meeting.model.Agenda;
import com.jolupbisang.demo.domain.meeting.model.Meeting;

public record AgendaChangedEvent(
        Agenda agenda,
        Meeting meeting
) {
}
