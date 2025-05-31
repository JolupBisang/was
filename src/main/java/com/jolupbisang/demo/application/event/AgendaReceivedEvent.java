package com.jolupbisang.demo.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class AgendaReceivedEvent extends ApplicationEvent {
    private final long meetingId;
    private final List<Integer> agenda;

    public AgendaReceivedEvent(Object source, long meetingId, List<Integer> agenda) {
        super(source);
        this.meetingId = meetingId;
        this.agenda = agenda;
    }

}
