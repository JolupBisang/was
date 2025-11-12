package com.jolupbisang.demo.domain.team.model;

import com.jolupbisang.demo.domain.team.exception.TeamDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private TeamMemberRole role;

    public TeamMember(Team team, Long userId, TeamMemberRole role) {
        setTeam(team);
        setUserId(userId);
        setRole(role);
    }

    private void setTeam(Team team) {
        if (team == null) {
            throw new DomainException(TeamDomainErrorCode.NULL_TEAM);
        }
        this.team = team;
    }

    private void setUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new DomainException(TeamDomainErrorCode.INVALID_USER_ID, "userId: %d", userId);
        }
        this.userId = userId;
    }

    private void setRole(TeamMemberRole role) {
        if (role == null) {
            throw new DomainException(TeamDomainErrorCode.NULL_TEAM_MEMBER_ROLE);
        }
        this.role = role;
    }
}
