package com.jolupbisang.demo.domain.meetingUser;

import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MeetingUser {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "meeting_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isHost;

    @Enumerated(EnumType.STRING)
    private MeetingUserStatus status;

    private String recordUrl;

    public MeetingUser(Meeting meeting, User user, boolean isHost, MeetingUserStatus status) {
        this.meeting = meeting;
        this.user = user;
        this.isHost = isHost;
        this.status = status;
    }

    public void updateRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }
}
