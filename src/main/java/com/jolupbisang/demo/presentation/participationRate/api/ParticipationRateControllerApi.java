package com.jolupbisang.demo.presentation.participationRate.api;

import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "참여율", description = "회의 참여율 관련 API")
public interface ParticipationRateControllerApi {

    @Operation(summary = "참여율 구독", description = "특정 회의의 참여율 이벤트를 구독합니다 (Server-Sent Events)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 성공",
                    content = @Content(mediaType = "text/event-stream", examples = {
                            @ExampleObject(name = "구독 성공", value = """
                                    connected to FEEDBACK event stream
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "진행중인 회의가 아님",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "구독 실패", value = """
                                        {
                                            "message": "진행중인 회의가 아닙니다.",
                                            "errorId": "0d61a27e-a496-4ca2-8617-07acda9bf570",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "참여자가 아닌 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "구독 실패", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "570cd8f2-8007-4f4e-bdbb-e9a5dd5d348e",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "존재하지 않는 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "구독 실패", value = """
                                        {
                                            "message": "존재하지 않는 회의입니다.",
                                            "errorId": "5cf2fd41-89c2-4362-92ea-84c5072b32a8",
                                            "errors": null
                                        }
                                    """),
                    })),
    })
    SseEmitter subscribe(
            @Parameter(description = "회의 ID", required = true, example = "1")
            @PathVariable(value = "meetingId") Long meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
