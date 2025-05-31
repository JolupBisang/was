package com.jolupbisang.demo.application.feedback.dto;

import com.jolupbisang.demo.application.summary.dto.SseSummaryRes;

import java.time.LocalDateTime;

public record SseFeedbackRes(
        LocalDateTime timestamp,
        String comment
) {

    public static SseFeedbackRes of(LocalDateTime timestamp, String comment) {
        return new SseFeedbackRes(timestamp, comment);
    }
}
