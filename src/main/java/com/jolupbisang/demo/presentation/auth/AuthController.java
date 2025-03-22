package com.jolupbisang.demo.presentation.auth;

import com.jolupbisang.demo.domain.auth.service.AuthService;
import com.jolupbisang.demo.domain.user.entity.OAuthPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/{platform}")
    public ResponseEntity<?> login(@RequestParam("code") String code,
                                   @PathVariable OAuthPlatform platform) {

        String accessToken = authService.loginWithOAuth(platform, code);

        return ResponseEntity.ok(accessToken);
    }
}
