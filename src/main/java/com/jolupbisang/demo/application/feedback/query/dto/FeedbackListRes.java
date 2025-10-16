package com.jolupbisang.demo.application.feedback.query.dto;

import com.jolupbisang.demo.domain.feedback.Feedback;

import java.time.LocalDateTime;

public record FeedbackListRes(
        Long id,
        String comment,
        LocalDateTime generatedDateTime
) {

    public static FeedbackListRes from(Feedback feedback) {
        return new FeedbackListRes(
                feedback.getId(),
                feedback.getComment(),
                feedback.getGeneratedDateTime()
        );
    }
} 
