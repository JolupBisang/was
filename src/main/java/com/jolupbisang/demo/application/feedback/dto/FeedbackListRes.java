package com.jolupbisang.demo.application.feedback.dto;

import com.jolupbisang.demo.domain.feedback.Feedback;

import java.time.LocalDateTime;

public record FeedbackListRes(
        Long id,
        String comment,
        LocalDateTime timestamp
) {

    public static FeedbackListRes from(Feedback feedback) {
        return new FeedbackListRes(
                feedback.getId(),
                feedback.getComment(),
                feedback.getTimestamp()
        );
    }
} 
