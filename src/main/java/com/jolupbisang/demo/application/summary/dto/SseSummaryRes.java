package com.jolupbisang.demo.application.summary.dto;

import java.time.LocalDateTime;

public record SseSummaryRes(
        LocalDateTime timestamp,
        String summary
) {

    public static SseSummaryRes of(LocalDateTime timestamp, String summary) {
        return new SseSummaryRes(timestamp, summary);
    }
}
