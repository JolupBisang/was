package com.jolupbisang.demo.domain.team.model;

import com.jolupbisang.demo.domain.team.exception.TeamDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TeamName teamName;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<TeamMember> members = new ArrayList<>();

    public Team(TeamName teamName) {
        setTeamName(teamName);
    }

    private void setTeamName(TeamName teamName) {
        if (teamName == null) {
            throw new DomainException(TeamDomainErrorCode.NULL_TEAM_NAME);
        }
        this.teamName = teamName;
    }
}
