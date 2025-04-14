package com.jolupbisang.demo.presentation.meeting.dto;

import com.jolupbisang.demo.application.meeting.dto.MeetingDetailSummary;

import java.util.ArrayList;
import java.util.List;

public record MeetingDetailSummaryRes(
        List<MeetingDetailSummary> meetings
) {

    public static MeetingDetailSummaryRes fromDto(List<MeetingDetailSummary> meetings) {
        return new MeetingDetailSummaryRes(
                new ArrayList<>(meetings)
        );
    }
}
