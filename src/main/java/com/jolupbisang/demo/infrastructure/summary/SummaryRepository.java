package com.jolupbisang.demo.infrastructure.summary;

import com.jolupbisang.demo.domain.summary.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
}
