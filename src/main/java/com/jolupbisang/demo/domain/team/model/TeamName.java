package com.jolupbisang.demo.domain.team.model;

import com.jolupbisang.demo.domain.team.exception.TeamDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class TeamName {

    @Column(name = "name")
    private String name;

    public TeamName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException(TeamDomainErrorCode.INVALID_TEAM_NAME, "name:%s", name);
        }
        this.name = name;
    }
}
