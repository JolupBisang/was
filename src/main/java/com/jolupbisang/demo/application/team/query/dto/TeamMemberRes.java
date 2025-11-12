package com.jolupbisang.demo.application.team.query.dto;

import com.jolupbisang.demo.domain.user.User;

import java.util.List;

public record TeamMemberRes(
        List<Member> members
) {
    public static TeamMemberRes from(List<User> users) {
        List<Member> members = users.stream()
                .map(user -> new Member(
                        user.getId(),
                        user.getNickname(),
                        user.getPictureURL()
                ))
                .toList();

        return new TeamMemberRes(members);
    }

    private record Member(
            Long id,
            String name,
            String pictureURL
    ) {
    }
}

