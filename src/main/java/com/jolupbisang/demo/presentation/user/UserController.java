package com.jolupbisang.demo.presentation.user;

import com.jolupbisang.demo.application.user.service.UserService;
import com.jolupbisang.demo.global.response.SuccessResponse;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.user.api.UserControllerApi;
import com.jolupbisang.demo.presentation.user.dto.response.UserInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerApi {

    private final UserService userService;

    @Override
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserInfo(@PathVariable("email") String email) {
        UserInfoRes userInfo = UserInfoRes.from(userService.findByEmail(email));

        return ResponseEntity.ok(SuccessResponse.of("회원 조회 성공", userInfo));
    }

    @GetMapping("/my-profile")
    @Override
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {

        UserInfoRes userInfo = UserInfoRes.from(userService.findByEmail(userDetails.getEmail()));

        return ResponseEntity.ok(SuccessResponse.of("회원 조회 성공", userInfo));
    }
}
