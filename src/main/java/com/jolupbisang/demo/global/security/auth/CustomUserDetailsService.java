package com.jolupbisang.demo.global.security.auth;

import com.jolupbisang.demo.domain.user.dto.UserInfoRes;
import com.jolupbisang.demo.domain.user.service.UserService;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfoRes user = userService.findById(email);
        return new CustomUserDetails(
                user.id(),
                user.email(),
                user.nickname()
        );
    }
}
