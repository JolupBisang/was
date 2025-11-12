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

    public Team(TeamName teamName, Long creatorId, List<Long> memberIds) {
        setTeamName(teamName);
        setMembers(creatorId, memberIds);
    }

    private void setTeamName(TeamName teamName) {
        if (teamName == null) {
            throw new DomainException(TeamDomainErrorCode.NULL_TEAM_NAME);
        }
        this.teamName = teamName;
    }

    private void setMembers(Long creatorId, List<Long> memberIds) {
        if (creatorId == null || creatorId <= 0) {
            throw new DomainException(TeamDomainErrorCode.INVALID_USER_ID, "creatorId: %d", creatorId);
        }
        if (memberIds == null) {
            throw new DomainException(TeamDomainErrorCode.EMPTY_TEAM_MEMBERS);
        }

        // 생성자는 TEAM_OWNER 역할로 추가
        members.add(new TeamMember(this, creatorId, TeamMemberRole.TEAM_OWNER));

        // 리스트에서 생성자 제외하고 중복 제거 후 TEAM_MEMBER로 추가
        List<Long> distinctMemberIds = memberIds.stream()
                .filter(memberId -> !memberId.equals(creatorId))
                .distinct()
                .toList();

        distinctMemberIds.forEach(memberId ->
                members.add(new TeamMember(this, memberId, TeamMemberRole.TEAM_MEMBER))
        );
    }

    public void addMember(Long userId, TeamMemberRole role) {
        if (isMember(userId)) return;
        
        TeamMember member = new TeamMember(this, userId, role);
        this.members.add(member);
    }

    public boolean isMember(Long userId) {
        return members.stream()
                .anyMatch(member -> member.getUserId().equals(userId));
    }

    public void validateViewAuthority(Long accessUserId) {
        if (!isMember(accessUserId)) {
            throw new DomainException(TeamDomainErrorCode.NOT_MEMBER, "userId: %d", accessUserId);
        }
    }

    public boolean isOwner(Long userId) {
        return members.stream()
                .anyMatch(member -> member.getUserId().equals(userId) && member.getRole() == TeamMemberRole.TEAM_OWNER);
    }

    public void validateOwnerAuthority(Long accessUserId) {
        if (!isOwner(accessUserId)) {
            throw new DomainException(TeamDomainErrorCode.ONLY_FOR_OWNER_AUTHORITY, "userId: %d", accessUserId);
        }
    }
}
