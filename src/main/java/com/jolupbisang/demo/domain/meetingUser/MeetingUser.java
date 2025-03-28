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

    public MeetingUser(Meeting meeting, User user, boolean isHost, MeetingUserStatus status) {
        this.meeting = meeting;
        this.user = user;
        this.isHost = isHost;
        this.status = status;
    }


    //서비스 두개를 합친 서비스
    //서비스를 더 잘게 쪼갠 애들을 하나의 서비스가 사용
}
