package com.jolupbisang.demo.presentation.meeting.dto.response;

import com.jolupbisang.demo.application.meeting.dto.MeetingDetailSummary;

import java.util.List;

public record MeetingDetailSummaryRes(
        List<DetailDto> meetings
) {

    public static MeetingDetailSummaryRes fromDto(List<MeetingDetailSummary> meetings) {
        return new MeetingDetailSummaryRes(
                meetings.stream()
                        .map(detail ->
                                new DetailDto(
                                        detail.id(),
                                        detail.title(),
                                        detail.location(),
                                        detail.scheduledStartTime(),
                                        detail.targetTime(),
                                        detail.status()
                                ))
                        .toList()
        );
    }

    private record DetailDto(
            Long id,
            String title,
            String location,
            String scheduledStartTime,
            int targetTime,
            String status
    ) {
    }
}
