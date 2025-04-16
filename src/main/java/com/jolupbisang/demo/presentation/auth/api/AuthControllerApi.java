package com.jolupbisang.demo.presentation.auth.api;

import com.jolupbisang.demo.domain.user.OAuthPlatform;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "인증", description = "사용자 인증 관련 API")
public interface AuthControllerApi {

    @Operation(summary = "앱용 OAuth 로그인", description = "앱 클라이언트를 위한 OAuth 로그인을 처리하고 앱으로 리디렉션")
    RedirectView loginThroughApp(
            @Parameter(description = "OAuth 인증 코드", required = true)
            @RequestParam("code") String code,
            @Parameter(description = "OAuth 플랫폼", required = true, schema = @Schema(allowableValues = {"GOOGLE"}))
            @PathVariable OAuthPlatform platform);

    @Operation(summary = "웹용 OAuth 로그인", description = "웹 클라이언트를 위한 OAuth 로그인을 처리하고 JWT 토큰을 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 반환",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "로그인 성공", value = """
                                        <<JWT 토큰>>
                                    """),
                    })
            ),
    })
    ResponseEntity<?> loginThroughWeb(
            @Parameter(description = "OAuth 인증 코드", required = true)
            @RequestParam("code") String code,
            @Parameter(description = "OAuth 플랫폼", required = true, schema = @Schema(allowableValues = {"GOOGLE"}))
            @PathVariable OAuthPlatform platform);
}
