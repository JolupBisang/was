package com.jolupbisang.demo.domain.user.service;

import com.jolupbisang.demo.domain.user.dto.UserInfoRes;
import com.jolupbisang.demo.domain.user.exception.UserErrorCode;
import com.jolupbisang.demo.domain.user.repository.UserRepository;
import com.jolupbisang.demo.global.common.exception.CustomException;
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
