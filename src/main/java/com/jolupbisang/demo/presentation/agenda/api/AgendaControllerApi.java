package com.jolupbisang.demo.presentation.agenda.api;

import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.agenda.dto.request.AgendaStatusReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "회의 안건", description = "회의 안건 관련 API")
public interface AgendaControllerApi {

    @Operation(summary = "회의 안건 상태 변경", description = "회의 안건의 완료 상태를 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "안건 상태 변경 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "변경 성공", value = """
                                        {
                                            "message": "회의 안건 상태변경 성공",
                                            "data": {
                                                "isCompleted": true
                                            }
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "422", description = "DTO 검증 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "변경 실패", value = """
                                        {
                                            "message": "잘못된 입력입니다.",
                                            "errorId": "dc0bf0b6-484c-4151-9597-eb10d57cba91",
                                            "errors": {
                                                "isCompleted": "완료 상태는 null일 수 없습니다."
                                            }
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 또는 없는 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "변경 실패", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "5d458520-6cd7-404f-ab91-234bd05fbb66",
                                            "errors": null
                                        }
                                    """),
                    })),
    })
    ResponseEntity<?> changeAgendaStatus(AgendaStatusReq agendaStatusReq,
                                         @PathVariable("agendaId") Long agendaId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails);

    @Operation(summary = "회의 안건 목록 조회", description = "특정 회의의 안건 목록을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안건 목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 성공", value = """
                                        {
                                            "message": "회의 안건 조회 성공",
                                            "data": {
                                                "agendaDetails": [
                                                    {
                                                        "agendaId": 1,
                                                        "content": "회의 안건1",
                                                        "isCompleted": true
                                                    },
                                                    {
                                                        "agendaId": 2,
                                                        "content": "회의 안건2",
                                                        "isCompleted": false
                                                    }
                                                ]
                                            }
                                        }
                                    """),
                    })),
            @ApiResponse(responseCode = "403", description = "권한 없음 또는 없는 회의",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "조회 실패", value = """
                                        {
                                            "message": "해당 회의의 참여자가 아닙니다.",
                                            "errorId": "4109fecb-a380-45a8-a200-0bc2ce01ce76",
                                            "errors": null
                                        }
                                    """),
                    })),
    })
    ResponseEntity<?> getAgendas(@PathVariable("meetingId") Long meetingId,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails);
}
