package com.jolupbisang.demo.global.config;

import com.jolupbisang.demo.global.event.Events;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventConfiguration {

    public EventConfiguration(ApplicationEventPublisher eventPublisher) {
        Events.setPublisher(eventPublisher);
    }
}
