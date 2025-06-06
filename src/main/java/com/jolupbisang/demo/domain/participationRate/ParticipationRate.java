package com.jolupbisang.demo.domain.participationRate;

import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "participation_rate")
public class ParticipationRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_rate_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private Long totalParticipationTime;

    public ParticipationRate(Meeting meeting, User user, Double rate, Long totalParticipationTime) {
        this.meeting = meeting;
        this.user = user;
        this.rate = rate;
        this.totalParticipationTime = totalParticipationTime;
    }
} 
