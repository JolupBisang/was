package com.jolupbisang.demo.infrastructure.team;

import com.jolupbisang.demo.domain.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
    
    List<Team> findByMembersUserId(Long userId);
}
