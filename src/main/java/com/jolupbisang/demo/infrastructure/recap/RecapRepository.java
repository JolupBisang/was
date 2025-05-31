package com.jolupbisang.demo.infrastructure.recap;

import com.jolupbisang.demo.domain.recap.Recap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecapRepository extends JpaRepository<Recap, Long> {
}
