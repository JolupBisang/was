package com.jolupbisang.demo.domain.meeting;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime scheduledStartTime;

    @Column(nullable = false)
    private int targetTime;

    @Column(nullable = false)
    private int restInterval;

    @Column(nullable = false)
    private int restDuration;

    @Enumerated(EnumType.STRING)
    private MeetingStatus meetingStatus;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private String recordUrl;

    public Meeting(String title, String location, LocalDateTime scheduledStartTime, int targetTime, int restInterval, int restDuration) {
        this.title = title;
        this.location = location;
        this.scheduledStartTime = scheduledStartTime;
        this.targetTime = targetTime;
        this.restInterval = restInterval;
        this.restDuration = restDuration;
    }
}
