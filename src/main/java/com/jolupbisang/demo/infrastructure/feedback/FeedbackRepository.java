package com.jolupbisang.demo.infrastructure.feedback;

import com.jolupbisang.demo.domain.feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
