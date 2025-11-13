package com.jolupbisang.demo.application.team.query.dto;

import com.jolupbisang.demo.domain.user.User;

import java.util.List;

public record TeamMemberRes(
        List<MemberInfo> members
) {
    public static TeamMemberRes from(List<User> users) {
        List<MemberInfo> members = users.stream()
                .map(user -> new MemberInfo(
                        user.getId(),
                        user.getNickname(),
                        user.getEmail(),
                        user.getPictureURL()
                ))
                .toList();

        return new TeamMemberRes(members);
    }

    private record MemberInfo(
            Long id,
            String name,
            String email,
            String pictureURL
    ) {
    }
}

