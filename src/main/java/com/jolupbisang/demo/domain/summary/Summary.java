package com.jolupbisang.demo.domain.summary;

import com.jolupbisang.demo.domain.meeting.Meeting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    private String content;

    private boolean isRecap;

    private LocalDateTime timestamp;

    public Summary(Meeting meeting, String content, boolean isRecap, LocalDateTime timestamp) {
        this.meeting = meeting;
        this.content = content;
        this.isRecap = isRecap;
        this.timestamp = timestamp;
    }
}
