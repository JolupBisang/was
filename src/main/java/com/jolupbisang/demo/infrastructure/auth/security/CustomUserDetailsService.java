package com.jolupbisang.demo.infrastructure.auth.security;

import com.jolupbisang.demo.application.user.dto.UserInfoRes;
import com.jolupbisang.demo.application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserInfoRes user = userService.findById(Long.parseLong(userId));
        return new CustomUserDetails(
                user.id(),
                user.email(),
                user.nickname()
        );
    }
}
