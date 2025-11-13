package com.jolupbisang.demo.domain.meetingFolder.model;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import com.jolupbisang.demo.domain.meetingFolder.exception.MeetingFolderDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 회의록 폴더 관리 어그리거트 루트
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingFolder extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "meetingFolder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MeetingFolderMeeting> meetings = new ArrayList<>();

    public MeetingFolder(Long userId, String name, List<Long> meetingIds) {
        setUserId(userId);
        setName(name);
        if (meetingIds != null && !meetingIds.isEmpty()) {
            meetingIds.forEach(meetingId -> meetings.add(new MeetingFolderMeeting(this, meetingId)));
        }
    }

    public void addMeeting(Long meetingId) {
        if (meetingId == null) {
            throw new DomainException(MeetingFolderDomainErrorCode.NON_EXISTING_MEETING, "meetingId: null");
        }
        if (hasMeeting(meetingId)) {
            return;
        }
        meetings.add(new MeetingFolderMeeting(this, meetingId));
    }

    public void removeMeeting(Long meetingId) {
        if (meetingId == null) {
            throw new DomainException(MeetingFolderDomainErrorCode.NON_EXISTING_MEETING, "meetingId: null");
        }
        meetings.removeIf(m -> m.getMeetingId().equals(meetingId));
    }

    public boolean hasMeeting(Long meetingId) {
        return meetings.stream()
                .anyMatch(m -> m.getMeetingId().equals(meetingId));
    }

    public List<Long> getMeetingIds() {
        return meetings.stream()
                .map(MeetingFolderMeeting::getMeetingId)
                .toList();
    }

    public void updateName(String name) {
        setName(name);
    }

    public void validateOwnerAuthority(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new DomainException(MeetingFolderDomainErrorCode.NOT_FOLDER_OWNER, "userId: %d", userId);
        }
    }

    private void setUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new DomainException(MeetingFolderDomainErrorCode.INVALID_USER_ID, "userId: %d", userId);
        }
        this.userId = userId;
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException(MeetingFolderDomainErrorCode.EMPTY_FOLDER_NAME);
        }
        this.name = name;
    }
}


