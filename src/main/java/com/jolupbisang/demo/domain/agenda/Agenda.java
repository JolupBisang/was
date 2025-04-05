package com.jolupbisang.demo.domain.agenda;

import com.jolupbisang.demo.domain.meeting.Meeting;
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

    private boolean isCompleted;

    public Agenda(Meeting meeting, String content) {
        this.meeting = meeting;
        this.content = content;
        this.isCompleted = false;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = true;
    }
}
