package com.jolupbisang.demo.infrastructure.summary;

import com.jolupbisang.demo.domain.summary.Summary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Long> {

    Slice<Summary> findByMeetingIdAndIsRecap(Long meetingId, boolean isRecap, Pageable pageable);
}
