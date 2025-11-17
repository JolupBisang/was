package com.jolupbisang.demo.domain.summary;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import com.jolupbisang.demo.domain.summary.event.SummaryCreatedEvent;
import com.jolupbisang.demo.global.event.Events;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Summary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meeting_id")
    long meetingId;

    @Column(length = 1500)
    private String content;

    private boolean isRecap;

    private LocalDateTime generatedDateTime;

    public Summary(long meetingId, String content, boolean isRecap, LocalDateTime generatedDateTime) {
        this.meetingId = meetingId;
        this.content = content;
        this.isRecap = isRecap;
        this.generatedDateTime = generatedDateTime;

        if (!isRecap) {
            Events.raise(new SummaryCreatedEvent(meetingId, content, generatedDateTime));
        }
    }
}
