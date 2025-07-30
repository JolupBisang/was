package com.jolupbisang.demo.domain.feedback;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "meeting_id")
    private long meetingId;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "generated_date_time")
    LocalDateTime generatedDateTime;

    public Feedback(long userId, long meetingId, String comment, LocalDateTime generatedDateTime) {
        this.userId = userId;
        this.meetingId = meetingId;
        this.comment = comment;
        this.generatedDateTime = generatedDateTime;

    }
}
