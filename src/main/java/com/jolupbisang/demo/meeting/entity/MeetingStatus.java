package com.jolupbisang.demo.meeting.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingStatus {
    WAITING, IN_PROGRESS, COMPLETED, CANCELLED;
}
