package com.jolupbisang.demo.domain.meeting;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    private String recordUrl;

    public Meeting(String title, String location, LocalDateTime scheduledStartTime, int targetTime) {
        this.title = title;
        this.location = location;
        this.scheduledStartTime = scheduledStartTime;
        this.targetTime = targetTime;
    }
}
