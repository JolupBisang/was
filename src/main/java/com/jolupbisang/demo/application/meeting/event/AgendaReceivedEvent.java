package com.jolupbisang.demo.application.meeting.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class AgendaReceivedEvent extends ApplicationEvent {
    private final long meetingId;
    private final List<Long> agenda;

    public AgendaReceivedEvent(Object source, long meetingId, List<Long> agenda) {
        super(source);
        this.meetingId = meetingId;
        this.agenda = agenda;
    }

}
