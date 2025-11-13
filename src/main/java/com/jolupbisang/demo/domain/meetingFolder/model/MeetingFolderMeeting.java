package com.jolupbisang.demo.domain.meetingFolder.model;

import com.jolupbisang.demo.domain.meetingFolder.exception.MeetingFolderDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "meeting_folder_meeting")
public class MeetingFolderMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_folder_id", nullable = false)
    private MeetingFolder meetingFolder;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    public MeetingFolderMeeting(MeetingFolder meetingFolder, Long meetingId) {
        setMeetingFolder(meetingFolder);
        setMeetingId(meetingId);
    }

    private void setMeetingFolder(MeetingFolder meetingFolder) {
        if (meetingFolder == null) {
            throw new DomainException(MeetingFolderDomainErrorCode.NON_EXISTING_MEETING, "meetingFolder: null");
        }
        this.meetingFolder = meetingFolder;
    }

    private void setMeetingId(Long meetingId) {
        if (meetingId == null || meetingId <= 0) {
            throw new DomainException(MeetingFolderDomainErrorCode.NON_EXISTING_MEETING, "meetingId: %d", meetingId);
        }
        this.meetingId = meetingId;
    }
}

