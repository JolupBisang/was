package com.jolupbisang.demo.presentation.participationRate.api;

import com.jolupbisang.demo.application.participationRate.dto.ParticipationRateHistoryRes;
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

@Tag(name = "참여율", description = "회의 참여율 관련 API")
public interface ParticipationRateControllerApi {

    @Operation(summary = "최종 참여율 조회", description = "특정 회의의 최종 참여율 데이터를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참여율 히스토리 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "참여율 히스토리 조회 성공", value = """
                                        {
                                            "userParticipationRates": [
                                                {
                                                    "userId": 1,
                                                    "nickname": "김철수",
                                                    "rate": 45.67
                                                },
                                                {
                                                    "userId": 2,
                                                    "nickname": "이영희",
                                                    "rate": 32.15
                                                },
                                                {
                                                    "userId": 3,
                                                    "nickname": "박민수",
                                                    "rate": 22.18
                                                }
                                            ]
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "400", description = "해당 회의의 참여율 데이터가 존재하지 않음",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "참여율 데이터 없음", value = """
                                        {
                                            "message": "해당 회의의 참여율 데이터가 존재하지 않습니다.",
                                            "errorId": "1234-5678-9abc",
                                            "errors": null
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "참여자가 아닌 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "참여자 아님", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "570cd8f2-8007-4f4e-bdbb-e9a5dd5d348e",
                                            "errors": null
                                        }
                                    """),
                    })),
    })
    ParticipationRateHistoryRes getParticipationRateHistory(
            @Parameter(description = "회의 ID", required = true, example = "1")
            @PathVariable Long meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
