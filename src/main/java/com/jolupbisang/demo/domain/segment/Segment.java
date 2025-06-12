package com.jolupbisang.demo.domain.segment;

import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "segment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"meeting_id", "segment_order"})
})
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "segment_order", nullable = false)
    private int segmentOrder;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(length = 10, nullable = false)
    private String lang;

    public Segment(Meeting meeting, User user, int segmentOrder, LocalDateTime timestamp, String text, String lang) {
        this.meeting = meeting;
        this.user = user;
        this.segmentOrder = segmentOrder;
        this.timestamp = timestamp;
        this.text = text;
        this.lang = lang;
    }

    public void updateDetails(String text, String lang, User user) {
        this.text = text;
        this.lang = lang;
        this.user = user;
    }
} 
