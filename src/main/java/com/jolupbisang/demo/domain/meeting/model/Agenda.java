package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.event.AgendaStatusChangedEvent;
import com.jolupbisang.demo.global.event.Events;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    private String content;

    private Boolean isCompleted;

    public Agenda(Meeting meeting, String content) {
        this.meeting = meeting;
        this.content = content;
        this.isCompleted = false;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void changeStatus(boolean isCompleted) {
        if (this.isCompleted == isCompleted) {
            return;
        }
        this.isCompleted = isCompleted;
        Events.raise(new AgendaStatusChangedEvent(id, isCompleted));
    }
}
