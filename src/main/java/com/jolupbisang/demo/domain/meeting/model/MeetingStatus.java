package com.jolupbisang.demo.domain.meeting.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingStatus {
    WAITING, IN_PROGRESS, COMPLETED, CANCELLED;
}
