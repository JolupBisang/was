package com.jolupbisang.demo.presentation.auth;

import com.jolupbisang.demo.application.auth.service.AuthService;
import com.jolupbisang.demo.application.auth.service.ClientPlatform;
import com.jolupbisang.demo.domain.user.OAuthPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/app/{platform}")
    public RedirectView loginThroughApp(@RequestParam("code") String code,
                                        @PathVariable OAuthPlatform platform) {
        String jwt = authService.loginWithOAuth(platform, ClientPlatform.APP, code);
        String redirectUrl = "com.imhungry.jjongseol://oauth2callback?token=" + jwt;
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/login/web/{platform}")
    @ResponseBody
    public ResponseEntity<?> loginThroughWeb(@RequestParam("code") String code,
                                             @PathVariable OAuthPlatform platform) {
        String jwt = authService.loginWithOAuth(platform, ClientPlatform.WEB, code);
        return ResponseEntity.ok(jwt);
    }
}
