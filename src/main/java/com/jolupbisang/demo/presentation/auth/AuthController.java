package com.jolupbisang.demo.presentation.auth;

import com.jolupbisang.demo.application.auth.service.AuthService;
import com.jolupbisang.demo.domain.user.OAuthPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/{platform}")
    @ResponseBody
    public ResponseEntity<?> loginThroughWeb(@RequestParam("code") String code,
                                             @PathVariable OAuthPlatform platform) {
        String jwt = authService.loginWithOAuth(platform, code);
        return ResponseEntity.ok(jwt);
    }
}
