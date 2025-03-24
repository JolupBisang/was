package com.jolupbisang.demo.application.user.service;

import com.jolupbisang.demo.application.user.dto.UserInfoRes;
import com.jolupbisang.demo.application.user.exception.UserErrorCode;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfoRes findById(Long userId) {
        return UserInfoRes.from(userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND)));
    }

    public UserInfoRes findByEmail(String email) {
        return UserInfoRes.from(userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND)));
    }
}
