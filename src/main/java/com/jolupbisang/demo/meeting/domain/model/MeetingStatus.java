package com.jolupbisang.demo.meeting.domain.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingStatus {
    WAITING, IN_PROGRESS, COMPLETED, CANCELLED;
}
