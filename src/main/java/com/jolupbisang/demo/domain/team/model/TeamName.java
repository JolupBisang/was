package com.jolupbisang.demo.domain.team.model;

import com.jolupbisang.demo.domain.team.exception.TeamDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Embeddable;

@Embeddable
public record TeamName(
        String name
) {

    public TeamName {
        if (name == null || name.isBlank()) {
            throw new DomainException(TeamDomainErrorCode.INVALID_TEAM_NAME, "name:%d", name);
        }
    }
}
