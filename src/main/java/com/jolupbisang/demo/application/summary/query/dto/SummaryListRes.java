package com.jolupbisang.demo.application.summary.query.dto;

import com.jolupbisang.demo.domain.summary.Summary;

import java.time.LocalDateTime;

public record SummaryListRes(
        Long id,
        String content,
        boolean isRecap,
        LocalDateTime generatedDateTime
) {

    public static SummaryListRes from(Summary summary) {
        return new SummaryListRes(
                summary.getId(),
                summary.getContent(),
                summary.isRecap(),
                summary.getGeneratedDateTime()
        );
    }
} 
