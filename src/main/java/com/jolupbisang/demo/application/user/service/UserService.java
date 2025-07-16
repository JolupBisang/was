package com.jolupbisang.demo.application.user.service;

import com.jolupbisang.demo.application.user.dto.UserInfo;
import com.jolupbisang.demo.application.user.exception.UserErrorCode;
import com.jolupbisang.demo.global.exception.ServiceLogicException;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfo findById(Long userId) {
        return UserInfo.fromEntity(userRepository.findById(userId)
                .orElseThrow(() -> new ServiceLogicException(UserErrorCode.NOT_FOUND)));
    }

    public UserInfo findByEmail(String email) {
        return UserInfo.fromEntity(userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(UserErrorCode.NOT_FOUND)));
    }
}
