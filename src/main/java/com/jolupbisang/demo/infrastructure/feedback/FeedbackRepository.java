package com.jolupbisang.demo.infrastructure.feedback;

import com.jolupbisang.demo.domain.feedback.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    Slice<Feedback> findByMeetingId(Long meetingId, Pageable pageable);
}
