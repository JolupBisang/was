package com.jolupbisang.demo.application.event;

import com.jolupbisang.demo.meeting.entity.Agenda;
import com.jolupbisang.demo.meeting.entity.Meeting;

public record AgendaChangedEvent(
        Agenda agenda,
        Meeting meeting
) {
}
