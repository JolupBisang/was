package com.jolupbisang.demo.domain.user;

import com.jolupbisang.demo.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private OAuthPlatform platform;

    @Builder
    public User(String email, String nickname, OAuthPlatform platform) {
        this.email = email;
        this.nickname = nickname;
        this.platform = platform;
    }
}
