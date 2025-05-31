package com.jolupbisang.demo.domain.summary;

import com.jolupbisang.demo.domain.meeting.Meeting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Summary(Meeting meeting, String content, boolean isRecap) {
        this.meeting = meeting;
        this.content = content;
        this.isRecap = isRecap;
    }
}
